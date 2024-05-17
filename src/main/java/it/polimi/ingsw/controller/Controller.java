package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.listenerhandler.ListenerHandler;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.Lobby;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
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

    private ListenerHandler<VirtualView> listenerHandler;

    public Controller() {
        lobby = new Lobby();
        timerForSuspendedGame = new Timer();
        turnCompletion = new TurnCompletion();
        listenerHandler = new ListenerHandler<>();
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
     */
    public synchronized void handleConnection(String username, VirtualView user) {
        if (!validUsername(username)) {
            try {
                user.reportError(new InvalidUsernameException().getMessage());
                return;
            } catch (RemoteException remoteException) {
                System.err.println("Connection error");
                return;
            }
        }
        if (!lobby.isGameReady() || game.isFinished()) {
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
     */
    private void joinLobby(String username, VirtualView lobbyListener) {
        try {
            lobby.add(username, lobbyListener);
        } catch (InvalidUsernameException | FullLobbyException e) {
            try {
                lobbyListener.reportError(e.getMessage());
                return;
            } catch (RemoteException remoteException) {
                System.err.println("Connection error");
                return;
            }
        }

        listenerHandler.add(username, lobbyListener);

        if (lobby.isGameReady()) {
            game = lobby.createGame();
        }
    }

    private void joinGame(String username, VirtualView gameListener) {
        try {
            game.add(username, gameListener);
        } catch (InvalidUsernameException e) {
            try {
                gameListener.reportError(e.getMessage());
                return;
            } catch (RemoteException remoteException) {
                System.err.println("Connection error");
                return;
            }
        }

        listenerHandler.add(username, gameListener);

        turnCompletion.handleJoin(game);
        if (game.isActive()) {
            timerForSuspendedGame.cancel();
        }
    }

    public synchronized void handleDisconnection(String username) {
        if (!lobby.isGameReady()) {
            leaveLobby(username);
        } else {
            leaveGame(username);
        }
        listenerHandler.remove(username);
    }

    /**
     * Removes the user from the lobby.
     *
     * @param username the user's name who leaves the lobby.
     */
    private void leaveLobby(String username) {
        lobby.remove(username);
    }

    /**
     * Removes the user from the game.
     *
     * @param username the user's name.
     */
    private void leaveGame(String username) {
        try {
            game.remove(username);
        } catch (InvalidUsernameException e) {
            reportError(username, e.getMessage());
            return;
        }
        turnCompletion.handleLeave(game);
        if (!game.isActive()) {
            timerForSuspendedGame.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (Controller.this) {
                        game.terminateForInactivity();
                    }
                }
            }, Game.MAX_DELAY_FOR_SUSPENDED_GAME);
        }
    }


    /**
     * Places the starter card of the player in the chosen side.
     *
     * @param username the player's username.
     * @param side     the side of the starter card.
     */
    @Override
    public synchronized void placeStarter(String username, Side side) {
        if (lobby.isGameReady()) {
            try {
                game.placeStarter(username, side);
            } catch (InvalidPlayerActionException | InvalidGamePhaseException e) {
                reportError(username, e.getMessage());
            }
        }
    }

    /**
     * Assigns color to the player with <code>username</code>.
     *
     * @param username of the player who have chosen the color.
     * @param color    chosen by the player.
     */
    @Override
    public synchronized void chooseColor(String username, PlayerColor color) {
        if (lobby.isGameReady()) {
            try {
                game.assignColor(username, color);
            } catch (NonexistentPlayerException | InvalidColorException | InvalidPlayerActionException |
                     InvalidGamePhaseException e) {
                reportError(username, e.getMessage());
            }
        }
    }

    /**
     * Places the secret objective from one of the two available.
     *
     * @param username        of the player.
     * @param chosenObjective the chosen objective.
     */
    @Override
    public synchronized void placeObjectiveCard(String username, int chosenObjective) {
        if (chosenObjective < 0 || chosenObjective > 1) {
            reportError(username, "Choose a valid card");
            return;
        }
        if (lobby.isGameReady()) {
            try {
                game.placeObjectiveCard(username, chosenObjective);
            } catch (InvalidGamePhaseException | InvalidPlayerActionException e) {
                reportError(username, e.getMessage());
            }
        }
    }

    /**
     * Places the card on the side and position specified.
     *
     * @param username of the player.
     * @param frontId  of the card's front to place.
     * @param backId   of the card's back to place.
     * @param side     of the card.
     * @param position in the playground.
     */
    @Override
    public synchronized void placeCard(String username, int frontId, int backId, Side side, Position position) {
        if (lobby.isGameReady()) {
            Card cardToPlace;
            try {
                cardToPlace = game.getCard(username, frontId, backId);
            } catch (InvalidCardIdException e) {
                reportError(username, e.getMessage());
                return;
            }
            try {
                game.placeCard(username, cardToPlace, side, position);
            } catch (InvalidPlayerActionException | Playground.UnavailablePositionException |
                     Playground.NotEnoughResourcesException
                     | InvalidGamePhaseException | SuspendedGameException e) {
                reportError(username, e.getMessage());
            }
        }
    }

    /**
     * Draws a card from the selected place.
     *
     * @param username the username of the player.
     * @param idToDraw the id representing the deck or any of the face up card positions.
     */
    @Override
    public synchronized void draw(String username, int idToDraw) {
        if (idToDraw < 0 || idToDraw > 5) {
            reportError(username, "Not valid id");
            return;
        }
        if (lobby.isGameReady()) {
            if (idToDraw == 4) {
                try {
                    game.drawFromDeck(username, DeckType.GOLDEN);
                } catch (InvalidPlayerActionException | EmptyDeckException | InvalidGamePhaseException e) {
                    reportError(username, e.getMessage());
                }
            } else if (idToDraw == 5) {
                try {
                    game.drawFromDeck(username, DeckType.RESOURCE);
                } catch (InvalidPlayerActionException | EmptyDeckException | InvalidGamePhaseException e) {
                    reportError(username, e.getMessage());
                }
            } else {
                try {
                    game.drawFromFaceUpCards(username, idToDraw);
                } catch (InvalidPlayerActionException | InvalidFaceUpCardException | InvalidGamePhaseException e) {
                    reportError(username, e.getMessage());
                }
            }
        }
    }

    /**
     * Sends message from the author.
     *
     * @param message sent by the author.
     */
    @Override
    public synchronized void sendMessage(Message message) {
        if (lobby.isGameReady()) {
            try {
                game.registerMessage(message);
            } catch (InvalidMessageException e) {
                reportError(message.getSender(), e.getMessage());
            }
        }
    }

    @Override
    public synchronized void setPlayersNumber(String username, int playersNumber) {
        try {
            lobby.setNumPlayersToStartTheGame(playersNumber);
        } catch (InvalidPlayersNumberException e) {
            reportError(username, e.getMessage());
            return;
        }
        // the lobby may be already started
        if (lobby.isGameReady()) {
            game = lobby.createGame();
        }
    }

    private void reportError(String username, String errorDetails) {
        try {
            listenerHandler.get(username).reportError(errorDetails);
        } catch (RemoteException e) {
            System.err.println("Connection error");
            handleDisconnection(username);
        }
    }
}
