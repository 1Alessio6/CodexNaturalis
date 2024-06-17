package it.polimi.ingsw.model.card.Color;

/**
 * Invalid Color Exception is thrown when the player attempts to select a color that has already been selected
 */
public class InvalidColorException extends Exception {
    /**
     * Constructs an <code>InvalidColorException</code> with no detail message.
     */
    public InvalidColorException() {
        super("Invalid color");
    }

    /**
     * Constructs an <code>InvalidColorException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public InvalidColorException(String message) {
        super(message);
    }
}
