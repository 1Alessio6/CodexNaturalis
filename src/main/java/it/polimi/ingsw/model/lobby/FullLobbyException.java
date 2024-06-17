package it.polimi.ingsw.model.lobby;

/**
 * Full Lobby Exception is thrown when the game hasn't started but the lobby is full
 */
public class FullLobbyException extends Exception {
    /**
     * Constructs a <code>FullLobbyException</code> with no detail message.
     */
    public FullLobbyException() {
        super("Game hasn't started yet, but lobby is already full");
    }
}
