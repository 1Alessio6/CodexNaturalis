package it.polimi.ingsw.model.lobby;

import it.polimi.ingsw.model.listenerhandler.ListenerHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the lobby of the game.
 * The first player joining the lobby will be the creator of the lobby, therefore they'll be the one to choose the number of players.
 * if the creator crashes or disconnects before having set the number of players, the lobby will be reset; otherwise it's considered as a normal player leaving the lobby.
 * When there are enough players (the number matches the one chosen by the creator) the lobby will create the game with all the players in the lobby.
 */

public class Lobby {
    private final ListenerHandler<LobbyListener> listenerHandler;
    private String creator;

    int numPlayersToStartTheGame;
    private final int MAX_NUMBER = 4;
    private final int MIN_NUMBER = 2;

    private boolean isGameReady;

    private static final int INVALID_NUM_PLAYERS = -1;

    /**
     * Constructs a new lobby with no parameters provided.
     */
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
    public void add(String username, LobbyListener listener) throws IllegalArgumentException, FullLobbyException, InvalidUsernameException {
        if (!isValid(username)) {
            throw new InvalidUsernameException();
        }

        int numListener = listenerHandler.getNumListener();
        if (numListener == numPlayersToStartTheGame || numListener == MAX_NUMBER) {
            throw new FullLobbyException();
        }

        listenerHandler.add(username, listener);

        listenerHandler.notify(username, receiver -> receiver.resultOfLogin(true, ""));

        if (creator == null) {
            creator = username;
            listenerHandler.notify(creator, LobbyListener::updateCreator);
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

    public int getNumPlayersToStartTheGame() {
        return numPlayersToStartTheGame;
    }


    /**
     * Resets the lobby in case it crashes.
     */
    private List<String> resetLobby(String nameOfCreator) {
        System.out.println("Lobby crashed");
        listenerHandler.notifyBroadcast(LobbyListener::updateAfterLobbyCrash);

        List<String> toRemoves = listenerHandler.getIds();
        toRemoves.add(nameOfCreator);
        creator = null;
        numPlayersToStartTheGame = INVALID_NUM_PLAYERS;
        isGameReady = false;
        listenerHandler.clear();
        return toRemoves;
    }

    public List<String> getPlayersInLobby() {
        return listenerHandler.getIds();
    }

    /**
     * Removes <code>username</code> from the lobby.
     * If the creator leaves the lobby before choosing the number of players, the lobby will reset itself.
     *
     * @param username of the player to remove.
     * @return a list of usernames that have been removed.
     * It's not a single return value because of the lobby auto reset.
     * For which each player is removed.
     */
    public List<String> remove(String username) throws InvalidUsernameException {
        System.out.println("User " + username + " has left the lobby");
        if (listenerHandler.get(username) == null) {
            throw new InvalidUsernameException();
        }
        listenerHandler.remove(username);
        List<String> removedUsers = new ArrayList<>();
        if (creator != null && creator.equals(username) && numPlayersToStartTheGame == INVALID_NUM_PLAYERS) {
            removedUsers.addAll(resetLobby(username));
        } else {
            List<String> usernames = new ArrayList<>(listenerHandler.getIds());
            listenerHandler.notifyBroadcast(receiver -> receiver.showUpdatePlayersInLobby(usernames));
            removedUsers.add(username);
        }
        return removedUsers;
    }

    /**
     * Sets the maximum number of players in the lobby
     *
     * @param numPlayersToStartTheGame the maximum number of players in the lobby
     * @throws InvalidPlayersNumberException if the specified number doesn't respect the game's constraint: min 2 players, max 4 players.
     */
    public void setNumPlayersToStartTheGame(int numPlayersToStartTheGame) throws InvalidPlayersNumberException {
        if (numPlayersToStartTheGame < MIN_NUMBER || numPlayersToStartTheGame > MAX_NUMBER) {
            throw new InvalidPlayersNumberException();
        }
        // ensure only one set per lobby
        if (this.numPlayersToStartTheGame == INVALID_NUM_PLAYERS) {
            this.numPlayersToStartTheGame = numPlayersToStartTheGame;
        }
    }

}