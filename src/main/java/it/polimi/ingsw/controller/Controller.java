package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.listenerhandler.ListenerHandler;
import it.polimi.ingsw.model.lobby.*;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.ClientHandler;

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

    private ListenerHandler<ClientHandler> listenerHandler;

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

    public synchronized boolean isRegisteredUsername(String username) {
        return listenerHandler.getIds().contains(username);
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
        boolean isAccepted;
        if (!validUsername(username)) {
            try {
                user.resultOfLogin(false, username, "empty name is not allowed here");
                isAccepted = false;
            } catch (RemoteException remoteException) {
                System.err.println("Connection error");
                isAccepted = false;
            }
        } else if (!lobby.isGameReady()) {
            isAccepted = joinLobby(username, user);
        } else if (game.isFinished()) {
            game = null;
            lobby = new Lobby();
            isAccepted = joinLobby(username, user);
        } else {
            isAccepted = joinGame(username, user);
        }
        return isAccepted;
    }

    private void createGame() {
        List<String> usernames = lobby.getPlayersInLobby();
        int numPlayersToStartTheGame = lobby.getNumPlayersToStartTheGame();
        List<String> usersToGoInGame = usernames.subList(0, numPlayersToStartTheGame);
        List<String> exceededPlayers = usernames.subList(numPlayersToStartTheGame, usernames.size());
        game = new Game(usersToGoInGame);

        for (String user : usersToGoInGame) {
            try {
                game.add(user, listenerHandler.get(user));
            } catch (InvalidUsernameException e) {
                System.err.println("Username is declared invalid even if it shouldn't => server will die");
                e.printStackTrace();
            }
        }

        for (String user : exceededPlayers) {
            listenerHandler.notify(user, LobbyListener::showUpdateExceedingPlayer);
            listenerHandler.remove(user);
        }
    }

    /**
     * Joins user to the lobby.
     *
     * @param username the user's name who joins the lobby.
     */
    private boolean joinLobby(String username, ClientHandler lobbyListener) {
        boolean isJoined;
        try {
            lobby.add(username, lobbyListener);
            listenerHandler.add(username, lobbyListener);
            if (game == null && lobby.isGameReady()) {
                createGame();
            }
            isJoined = true;
        } catch (InvalidUsernameException | FullLobbyException e) {
            lobbyListener.resultOfLogin(false, e.getMessage());
            isJoined = false;
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
    private boolean joinGame(String username, ClientHandler gameListener) {
        try {
            game.add(username, gameListener);
        } catch (InvalidUsernameException e) {
            gameListener.resultOfLogin(false, e.getMessage());
            return false;
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
            List<String> removedUsers = lobby.remove(username);
            for (String user : removedUsers) {
                listenerHandler.remove(user);
            }
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
            listenerHandler.remove(username);
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
            createGame();
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
        ClientHandler toReport = listenerHandler.get(username);
        // if it's registered in the controller
        if (toReport != null) {
            toReport.reportError(errorDetails);
        }
    }
}
