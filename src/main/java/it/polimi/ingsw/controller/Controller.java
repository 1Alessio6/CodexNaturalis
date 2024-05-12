package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.card.InvalidCardIdException;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.Lobby;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.RemoteException;
import java.util.*;

/**
 * The Controller class manages the entry of players through the use of a lobby, as well as their subsequent
 * disconnection.
 * It also allows the player to make moves in the playground and to choose his/her own username and token color
 * before the start of a game.
 */
public class Controller implements EventListener, GameRequest {
    private Lobby lobby;
    private Game game;
    private Timer timerForSuspendedGame;
    private TurnCompletion turnCompletion;

    public Controller() {
        lobby = new Lobby();
        timerForSuspendedGame = new Timer();
    }

    private boolean validUsername(String username) {
        return username != null && !username.isEmpty();
    }

    /**
     * Handle connection of the user.
     * If the game is not already started, the user is added to the lobby;
     * otherwise to the game.
     * When the game ends, the new connection will trigger the start of a new match.
     *
     * @param user     the representation of the user.
     * @param username the user's name.
     * @throws FullLobbyException       if the lobby is full.
     * @throws InvalidUsernameException if the username provided is null or an empty string.
     * @throws RemoteException          if the user disconnects after calling the method.
     */
    public void handleConnection(String username, VirtualView user) throws FullLobbyException, InvalidUsernameException, RemoteException {
        if (!validUsername(username)) {
            throw new InvalidUsernameException();
        }
        if (game == null || game.isFinished()) {
            game = null;
            joinLobby(username, user);
        } else {
            joinGame(username, user);
        }
    }

    /**
     * Joins user to the lobby.
     *
     * @param username the user's name who joins the lobby.
     * @throws FullLobbyException       if the lobby is full.
     * @throws InvalidUsernameException if there's already a player with <code>username</code> in the lobby.
     */
    private void joinLobby(String username, VirtualView lobbyListener) throws FullLobbyException, InvalidUsernameException, RemoteException {
        lobby.add(username, lobbyListener);
        if (lobby.isGameReady()) {
            game = lobby.createGame();
        }
    }

    private void joinGame(String username, VirtualView gameListener) throws InvalidUsernameException {
        game.add(username, gameListener);
        turnCompletion.handleJoin(game);
        if (game.isActive()) {
            timerForSuspendedGame.cancel();
        }
    }

    public void handleDisconnection(String username) throws RemoteException, InvalidUsernameException {
        if (this.game == null) {
            leaveLobby(username);
        } else {
            leaveGame(username);
        }
    }

    /**
     * Removes the user from the lobby.
     *
     * @param username the user's name who leaves the lobby.
     */
    private void leaveLobby(String username) throws RemoteException {
        lobby.remove(username);
    }

    /**
     * Removes the user from the game.
     *
     * @param username the user's name.
     */
    private void leaveGame(String username) throws InvalidUsernameException, RemoteException {
        game.remove(username);
        turnCompletion.handleLeave(game);
        if (!game.isActive()) {
            timerForSuspendedGame.schedule(new TimerTask() {
                @Override
                public void run() {
                    game.terminateForInactivity();
                }
            }, Game.MAX_DELAY_FOR_SUSPENDED_GAME);
        }
    }


    /**
     * Places the starter card of the player in the chosen side.
     *
     * @param username the player's username.
     * @param side     the side of the starter card.
     * @throws InvalidPlayerActionException if the player cannot perform this action.
     * @throws InvalidGamePhaseException    if the player has already finished their setup.
     */
    @Override
    public void placeStarter(String username, Side side) throws InvalidPlayerActionException, InvalidGamePhaseException {
        game.placeStarter(username, side);
    }

    /**
     * Assigns color to the player with <code>username</code>.
     *
     * @param username of the player who have chosen the color.
     * @param color    chosen by the player.
     * @throws InvalidColorException        if the color has already been chosen.
     * @throws InvalidPlayerActionException if the player cannot perform this action.
     * @throws InvalidGamePhaseException    if the player has already finished their setup.
     */
    @Override
    public void chooseColor(String username, PlayerColor color) throws NonexistentPlayerException, InvalidColorException, InvalidPlayerActionException, InvalidGamePhaseException, RemoteException {
        game.assignColor(username, color);
    }

    /**
     * Places the secret objective from one of the two available.
     *
     * @param username        of the player.
     * @param chosenObjective the chosen objective.
     * @throws InvalidPlayerActionException if the player cannot perform the operation. For example the player has already chosen the objective.
     * @throws InvalidGamePhaseException    if the player has already finished the setup.
     */
    @Override
    public void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException, InvalidGamePhaseException {
        if (chosenObjective < 0 || chosenObjective > 1) {
            throw new IllegalArgumentException();
        }
        game.placeObjectiveCard(username, chosenObjective);
    }

    /**
     * Places the card on the side and position specified.
     *
     * @param username of the player.
     * @param frontId  of the card's front to place.
     * @param backId   of the card's back to place.
     * @param side     of the card.
     * @param position in the playground.
     * @throws InvalidPlayerActionException            if the player cannot perform the operation.
     * @throws Playground.UnavailablePositionException if the position is not available. For example the player is trying to place the card in an already covered corner.
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card.
     * @throws InvalidGamePhaseException               if the game phase cannot allow placing cards.
     * @throws SuspendedGameException                  if the game is suspended, thus no placement is allowed.
     */
    @Override
    public void placeCard(String username, int frontId, int backId, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, InvalidCardIdException {
        Card cardToPlace = game.getCard(username, frontId, backId);
        game.placeCard(username, cardToPlace, side, position);
    }

    /**
     * Draws a card from the selected place.
     *
     * @param username the username of the player.
     * @param idToDraw the id representing the deck or any of the face up card positions.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException           if the selected deck is empty.
     * @throws InvalidGamePhaseException    if the game phase doesn't allow the operation.
     */
    @Override
    public void draw(String username, int idToDraw) throws InvalidPlayerActionException, EmptyDeckException, InvalidGamePhaseException, InvalidIdForDrawingException {
        if (idToDraw < 0 || idToDraw > 5) {
            throw new InvalidIdForDrawingException();
        }
        if (idToDraw == 4) {
            game.drawFromDeck(username, DeckType.GOLDEN);
        } else if (idToDraw == 5) {
            game.drawFromDeck(username, DeckType.RESOURCE);
        } else {
            game.drawFromFaceUpCards(username, idToDraw);
        }
    }

    /**
     * Sends message from the author.
     *
     * @param message sent by the author.
     * @throws InvalidMessageException if the message is invalid.
     */
    @Override
    public void sendMessage(Message message) throws InvalidMessageException {
        game.registerMessage(message);
    }

    @Override
    public void setPlayersNumber(int playersNumber) throws InvalidPlayersNumberException {
        lobby.setNumPlayersToStartTheGame(playersNumber);
    }
}
