package it.polimi.ingsw.model.lobby;

import java.util.*;

public class Lobby {
    private List<String> waitingList;

    int numPlayers;
    private final int MAX_NUMBER = 4;
    private final int MIN_NUMBER = 2;

    public Lobby() {
        waitingList = new ArrayList<>();
        this.numPlayers = 0;
    }

    /**
     * Joins <code>username</code> to the lobby.
     *
     * @param username of the player who joins the lobby.
     * @return the updated list of players' name who has joined the lobby.
     * @throws IllegalArgumentException if the <code>username</code> is <code>null</code> or an empty string.
     * @throws AlreadyInLobbyException  if the username is associated to a player who has joined the lobby.
     * @throws FullLobbyException       if the lobby contains 4 players or the number chosen by the creator of the lobby.
     */
    public List<String> joinLobby(String username) throws IllegalArgumentException, FullLobbyException, AlreadyInLobbyException {
        if (username == null || username.equals("")) {
            throw new IllegalArgumentException();
        }
        if (waitingList.contains(username)) {
            throw new AlreadyInLobbyException("Username already exists");
        }
        if (waitingList.size() == numPlayers
                || waitingList.size() == MAX_NUMBER) {
            throw new FullLobbyException();
        }

        waitingList.add(username);

        return waitingList;
    }

    /**
     * Resets the lobby. It happens when the creator leaves the lobby.
     *
     * @return players' username who's waiting in the lobby.
     */
    public List<String> resetLobby() {
        List<String> usersToNotify = new ArrayList<>(waitingList);
        waitingList.clear();
        return usersToNotify;
    }

    /**
     * Removes <coed>username</coed> from the lobby.
     *
     * @param username of the player to remove.
     * @return the updated list of players' name who has joined the lobby.
     */
    public List<String> remove(String username) {
        if (waitingList.indexOf(username) == 0) {
            return resetLobby();
        }
        waitingList.remove(username);
        return waitingList;
    }

    /**
     * Sets the maximum number of players in the lobby
     *
     * @param numPlayers the maximum number of players in the lobby
     * @throws IllegalArgumentException if the specified number doesn't respect the game's constraint: min 2 players, max 4 players.
     */
    public void setNumPlayers(int numPlayers) throws IllegalArgumentException {
        if (numPlayers < MIN_NUMBER || numPlayers > MAX_NUMBER) {
            throw new IllegalArgumentException();
        }

        // method is called once.
        this.numPlayers = numPlayers;
    }
}
