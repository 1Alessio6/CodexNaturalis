package it.polimi.ingsw.model;

/**
 * Suspended Game Exception is thrown when the player attempts to perform an action, but the game is suspended
 */
public class SuspendedGameException extends Exception {
    public SuspendedGameException() {
        super("You can't play now, the game is suspended");
    }

    public SuspendedGameException(String message) {
        super(message);
    }
}
