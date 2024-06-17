package it.polimi.ingsw.controller;

/**
 * Invalid ID For Drawing Exception is thrown when the card id isn't valid for drawing
 */
public class InvalidIdForDrawingException extends Exception {
    /**
     * Constructs an <code>InvalidIdForDrawingException</code> with no detail message.
     */
    public InvalidIdForDrawingException() {
        super("Invalid id for drawing");
    }
}
