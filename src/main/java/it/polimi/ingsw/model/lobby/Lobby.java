package it.polimi.ingsw.model.lobby;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.listenerhandler.ListenerHandler;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the lobby of the game.
 * The first player joining the lobby will be the creator of the lobby, therefore they'll be the one to choose the number of players.
 * if the creator crashes or disconnects before having set the number of players, the lobby will be reset; otherwise it's considered as a normal player leaving the lobby.
 * When there are enough players (the number matches the one chosen by the creator) the lobby will create the game with all the players in the lobby.
 */

public class Lobby {
    private final ListenerHandler<VirtualView> listenerHandler;
    private String creator;

    int numPlayersToStartTheGame;
    private final int MAX_NUMBER = 4;
    private final int MIN_NUMBER = 2;

    private boolean isGameReady;

    private static final int INVALID_NUM_PLAYERS = -1;

    public Lobby() {
        numPlayersToStartTheGame = INVALID_NUM_PLAYERS;
        creator = null;
        isGameReady = false;
        listenerHandler = new ListenerHandler<>();
    }

    private boolean isValid(String username) {
        return username != null
                && !username.isEmpty()
                && !listenerHandler.getIds().contains(username);
    }

    /**
     * Joins listener with <code>username</code> to the lobby.
     *
     * @param listener of the lobby events.
     * @param username of the player who joins the lobby.
     * @throws IllegalArgumentException if the <code>username</code> is <code>null</code> or an empty string.
     * @throws FullLobbyException       if the lobby contains 4 players or the number chosen by the creator of the lobby.
     */
    public void add(String username, VirtualView listener) throws IllegalArgumentException, FullLobbyException, InvalidUsernameException {
        int numListener = listenerHandler.getNumListener();
        if (numListener == numPlayersToStartTheGame || numListener == MAX_NUMBER) {
            throw new FullLobbyException();
        }
        if (!isValid(username)) {
            throw new InvalidUsernameException();
        }

        listenerHandler.add(username, listener);

        if (creator == null) {
            creator = username;
            listenerHandler.notify(creator, VirtualView::updateCreator);
        }
        List<String> usernames = new ArrayList<>(listenerHandler.getIds());
        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdatePlayersInLobby(usernames));
    }

    /**
     * Checks whether the game is ready or not, that is, there's sufficient players to start the game.
     *
     * @return true if the game is ready, false otherwise.
     */
    public boolean isGameReady() {
        isGameReady = numPlayersToStartTheGame != INVALID_NUM_PLAYERS && listenerHandler.getNumListener() >= numPlayersToStartTheGame;
        return isGameReady;
    }

    public Game createGame() {
        if (!isGameReady) {
            return null;
        }

        List<String> usernames = listenerHandler.getIds();
        List<String> usersToGoInGame = usernames.subList(0, numPlayersToStartTheGame);
        List<String> exceededPlayers = usernames.subList(numPlayersToStartTheGame, usernames.size());
        Game game = new Game(usersToGoInGame);

        for (String user : usersToGoInGame) {
            try {
                game.add(user, listenerHandler.get(user));
            } catch (InvalidUsernameException e) {
                System.err.println("Username is declared invalid even if it shouldn't ");
                e.printStackTrace();
            }
        }

        for (String user : exceededPlayers) {
            listenerHandler.notify(user, receiver -> receiver.reportError("The game is started with enough player: you are an exceeding player"));
        }

        return game;
    }

    private void resetLobby() {
        System.out.println("Lobby crashed");
        listenerHandler.notifyBroadcast(VirtualView::updateAfterLobbyCrash);

        listenerHandler.clear();
        creator = null;
        numPlayersToStartTheGame = INVALID_NUM_PLAYERS;
        isGameReady = false;
    }

    /**
     * Removes <code>username</code> from the lobby.
     * If the creator leaves the lobby before choosing the number of players, the lobby will reset itself.
     *
     * @param username of the player to remove.
     */
    public void remove(String username) {
        listenerHandler.remove(username);
        // if the creator leaves the lobby before setting the number of players
        if (creator != null && creator.equals(username) && numPlayersToStartTheGame == INVALID_NUM_PLAYERS) {
            resetLobby();
        } else {
            List<String> usernames = new ArrayList<>(listenerHandler.getIds());
            listenerHandler.notifyBroadcast(receiver -> receiver.showUpdatePlayersInLobby(usernames));
        }
    }

    /**
     * Sets the maximum number of players in the lobby
     *
     * @param numPlayersToStartTheGame the maximum number of players in the lobby
     * @throws IllegalArgumentException if the specified number doesn't respect the game's constraint: min 2 players, max 4 players.
     */
    public void setNumPlayersToStartTheGame(int numPlayersToStartTheGame) throws InvalidPlayersNumberException {
        if (numPlayersToStartTheGame < MIN_NUMBER || numPlayersToStartTheGame > MAX_NUMBER) {
            throw new InvalidPlayersNumberException();
        }
        // method is called once.
        this.numPlayersToStartTheGame = numPlayersToStartTheGame;
    }

    private void reportError(String username, String errorDetails) {
        try {
            listenerHandler.get(username).reportError(errorDetails);
        } catch (RemoteException e) {
            System.err.println("Connection error");
        }
    }

}