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
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.AlreadyInLobbyException;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.Lobby;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Side;

import java.util.EventListener;
import java.util.List;
import java.util.Set;

/**
 * The Controller class manages the entry of players through the use of a lobby, as well as their subsequent
 * disconnection.
 * It also allows the player to make moves in the playground and to choose his/her own username and token color
 * before the start of a game.
 */
public class Controller implements EventListener, GameRequest {
    private Lobby lobby;
    private Game game;
    private List<String> usernames;

    public Controller() {
        lobby = new Lobby();
    }

    /**
     * Joins user to the lobby.
     *
     * @param username the user's name who joins the lobby.
     * @throws FullLobbyException      if the lobby is full.
     * @throws AlreadyInLobbyException if there's already a player with <code>username</code> in the lobby.
     */
    private void joinLobby(String username) throws FullLobbyException, AlreadyInLobbyException {
        game = lobby.joinLobby(username);
    }

    // fixme(return_value): it has to be the client's representation of a player to recovery their state.
    private void joinGame(String username) {
        game.setNetworkStatus(username, true);
        // todo. get all info needed by the client related to the player and return them.
    }

    /**
     * Removes the user from the lobby.
     *
     * @param username the user's name who leaves the lobby.
     * @return true if the lobby has been reset, false otherwise.
     */
    @Override
    public boolean leaveLobby(String username) {
        List<String> leftUsers = lobby.remove(username);
        return leftUsers.isEmpty();
    }

    /**
     * Removes the user from the game.
     *
     * @param username the user's name.
     * @return true if there's no enough player, so the game has to be suspended; false otherwise.
     */
    @Override
    public boolean leaveGame(String username) {
        game.setNetworkStatus(username, false);
        // return true if the game need to be suspended.
        return game.getActivePlayers().size() <= 1;
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
     * @return the list of remaining colors after the player's choice.
     * @throws InvalidColorException        if the color has already been chosen.
     * @throws InvalidPlayerActionException if the player cannot perform this action.
     * @throws InvalidGamePhaseException    if the player has already finished their setup.
     */
    @Override
    public Set<PlayerColor> chooseColor(String username, PlayerColor color) throws NonexistentPlayerException, InvalidColorException, InvalidPlayerActionException, InvalidGamePhaseException {
        return game.assignColor(username, color);
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
        game.placeObjectiveCard(username, chosenObjective);
    }

    /**
     * Places the card on the side and position specified.
     *
     * @param username of the player.
     * @param frontId   of the card's front to place.
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
    public int placeCard(String username, int frontId, int backId, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException {
        Card cardToPlace = game.getCard(username, frontId, backId);
        return game.placeCard(username, cardToPlace, side, position);
    }

    /**
     * Draws a card from the selected place.
     *
     * @param username the username of the player.
     * @param idToDraw the id representing the deck or any of the face up card positions.
     * @return the emptiness of deck of draw/replace
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException           if the selected deck is empty.
     * @throws InvalidGamePhaseException    if the game phase doesn't allow the operation.
     */
    @Override
    public boolean draw(String username, int idToDraw) throws InvalidPlayerActionException, EmptyDeckException, InvalidGamePhaseException {
        boolean isEmpty = false;
        if (idToDraw == 4) {
            isEmpty = game.drawFromDeck(username, DeckType.GOLDEN);
        } else if (idToDraw == 5) {
            isEmpty = game.drawFromDeck(username, DeckType.RESOURCE);
        } else {
            isEmpty = game.drawFromFaceUpCards(username, idToDraw);
        }

        return isEmpty;
    }

    /**
     * Gets the current player from the game.
     *
     * @return the current player's name.
     */
    @Override
    public String getCurrentPlayer() {
        return game.getCurrentPlayer().getUsername();
    }


    /**
     * Skips the current player.
     */
    @Override
    public void skipTurn() {
        game.skipTurn();
    }

    /**
     * Sends message from the author.
     *
     * @param author  of the message.
     * @param message sent by the author.
     * @throws InvalidMessageException if the message is invalid.
     */
    @Override
    public void sendMessage(String author, Message message) throws InvalidMessageException {
        game.registerMessage(author, message);
    }

    @Override
    public void setPlayersNumber(int playersNumber) {
        lobby.setNumPlayers(playersNumber);

    }

    public List<String> getWinners() throws InvalidGamePhaseException {
        return game.getWinners();
    }
}
