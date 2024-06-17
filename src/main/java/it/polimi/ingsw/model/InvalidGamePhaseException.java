package it.polimi.ingsw.model;

/**
 * Invalid Game Phase Exception is thrown when an attempt is made to perform an action that is not allowed at a given phase of the game
 */
public class InvalidGamePhaseException extends Exception {
    /**
     * Constructs an <code>InvalidGamePhaseException</code> with no detail message
     */
    public InvalidGamePhaseException() {
        super("Invalid game phase");
    }

    /**
     * Constructs an <code>InvalidGamePhaseException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public InvalidGamePhaseException(String message) {
        super(message);
    }
}
