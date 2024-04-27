package it.polimi.ingsw.model.lobby;

import it.polimi.ingsw.model.Game;

import java.util.*;

public class Lobby {
    private List<String> waitingList;

    int numPlayers;
    private final int MAX_NUMBER = 4;
    private final int MIN_NUMBER = 2;

    public Lobby() {
        waitingList = new ArrayList<>();
        numPlayers = -1;
    }

    /**
     * Joins <code>username</code> to the lobby.
     *
     * @param username of the player who joins the lobby.
     * @return the Game if there are enough players, otherwise <code>null</code>.
     * @throws IllegalArgumentException if the <code>username</code> is <code>null</code> or an empty string.
     * @throws AlreadyInLobbyException  if the username is associated to a player who has joined the lobby.
     * @throws FullLobbyException       if the lobby contains 4 players or the number chosen by the creator of the lobby.
     */
    public Game joinLobby(String username) throws IllegalArgumentException, FullLobbyException, AlreadyInLobbyException {
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

        if (waitingList.size() == numPlayers) {
            return new Game(waitingList);
        }

        return null;
    }

    private void resetLobby() {
        waitingList.clear();
    }

    /**
     * Removes <code>username</code> from the lobby.
     * If the creator leaves the lobby before choosing the number of players, the lobby will reset itself.
     *
     * @param username of the player to remove.
     * @return the player's name left in the lobby; in case of a reset, the list will be empty.
     */
    public List<String> remove(String username) {
        // if the creator leaves the lobby before setting the number of players
        if (waitingList.indexOf(username) == 0 && numPlayers == -1) {
            resetLobby();
        } else {
            waitingList.remove(username);
        }

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
