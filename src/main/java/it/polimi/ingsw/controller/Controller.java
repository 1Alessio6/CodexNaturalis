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
import it.polimi.ingsw.model.player.Player;
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

    /**
     * Constructs a new <code>Controller</code> with no parameters provided.
     */
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
    public synchronized boolean handleConnection(String username, VirtualView user) {
        if (!validUsername(username)) {
            try {
                user.reportError(new InvalidUsernameException().getMessage());
                return false;
            } catch (RemoteException remoteException) {
                System.err.println("Connection error");
                return false;
            }
        }
        if (!lobby.isGameReady() || game.isFinished()) {
            game = null;
            return joinLobby(username, user);
        } else {
            return joinGame(username, user);
        }
    }

    /**
     * Removes excess players from registered users.
     */
    private void removeExceedingPlayers() {
        List<String> usersInGame = game.getPlayers().stream().map(Player::getUsername).toList();
        for (String registeredUser : listenerHandler.getIds()) {
            if (!usersInGame.contains(registeredUser)) {
                listenerHandler.remove(registeredUser);
            }
        }
    }

    /**
     * Joins user to the lobby.
     *
     * @param username the user's name who joins the lobby.
     */
    private boolean joinLobby(String username, VirtualView lobbyListener) {
        boolean isJoined;
        try {
            lobby.add(username, lobbyListener);
            listenerHandler.add(username, lobbyListener);
            if (game == null && lobby.isGameReady()) {
                game = lobby.createGame();
                removeExceedingPlayers();
            }
            isJoined = true;
        } catch (InvalidUsernameException | FullLobbyException e) {
            try {
                lobbyListener.reportError(e.getMessage());
                isJoined = false;
            } catch (RemoteException remoteException) {
                System.err.println("Connection error");
                isJoined = false;
            }
        }
        return isJoined;
    }

    /**
     * Joins <code>username</code> to the game
     *
     * @param username     of the player who joins the game.
     * @param gameListener the game listener
     * @return true if the player has been added correctly, false otherwise
     */
    private boolean joinGame(String username, VirtualView gameListener) {
        try {
            game.add(username, gameListener);
        } catch (InvalidUsernameException e) {
            try {
                gameListener.reportError(e.getMessage());
                return false;
            } catch (RemoteException remoteException) {
                System.err.println("Connection error");
                return false;
            }
        }

        listenerHandler.add(username, gameListener);

        turnCompletion.handleJoin(game);
        if (game.isActive()) {
            timerForSuspendedGame.cancel();
            timerForSuspendedGame = new Timer();
        }
        return true;
    }

    /**
     * Handle disconnection of the <code>username</code>.
     * If the game hasn't started, the <code>username</code> is removed from the lobby;
     * otherwise from the game.
     *
     * @param username the user's name.
     */
    public synchronized void handleDisconnection(String username) {
        if (!listenerHandler.getIds().contains(username)) {
            return;
        }

        listenerHandler.remove(username);

        if (!lobby.isGameReady()) {
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
    private void leaveLobby(String username) {
        try {
            lobby.remove(username);
        } catch (InvalidUsernameException e) {
            reportError(username, e.getMessage());
        }
    }

    /**
     * Removes the user from the game.
     *
     * @param username the user's name.
     */
    private void leaveGame(String username) {
        try {
            game.remove(username);
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
        } catch (InvalidUsernameException e) {
            reportError(username, e.getMessage());
        }
    }


    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setPlayersNumber(String username, int playersNumber) {
        try {
            lobby.setNumPlayersToStartTheGame(playersNumber);
        } catch (InvalidPlayersNumberException e) {
            reportError(username, e.getMessage());
            return;
        }
        if (game == null && lobby.isGameReady()) {
            game = lobby.createGame();
            removeExceedingPlayers();
        }
    }

    /**
     * Reports <code>errorDetails</code> to the <code>username</code> if registered, otherwise
     * it reports a connection error message.
     *
     * @param username     the name of the user to whom the error is to be reported.
     * @param errorDetails the details of the error.
     */
    private void reportError(String username, String errorDetails) {
        try {
            VirtualView toReport = listenerHandler.get(username);
            // if it's registered in the controller
            if (toReport != null) {
                toReport.reportError(errorDetails);
            }
        } catch (RemoteException e) {
            System.err.println("Connection error");
            handleDisconnection(username);
        }
    }
}
