package it.polimi.ingsw.model.player;

/**
 * Invalid Player Action Exception  is thrown when the player tries to perform an action that is not allowed
 */
public class InvalidPlayerActionException extends Exception {
    /**
     * Constructs an <code>InvalidPlayerActionException</code> with no detail message
     */
    public InvalidPlayerActionException()  {
        super("Invalid player action");
    }

    /**
     * Constructs an <code>InvalidPlayerActionException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public InvalidPlayerActionException(String message) {
        super(message);
    }
}
