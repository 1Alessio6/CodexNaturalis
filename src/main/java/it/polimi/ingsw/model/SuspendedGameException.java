package it.polimi.ingsw.model;

/**
 * Suspended Game Exception is thrown when the player attempts to perform an action, but the game is suspended
 */
public class SuspendedGameException extends Exception {
    /**
     * Constructs a <code>SuspendedGameException</code> with no detail message
     */
    public SuspendedGameException() {
        super("You can't play now, the game is suspended");
    }

    /**
     * Constructs a <code>SuspendedGameException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public SuspendedGameException(String message) {
        super(message);
    }
}
