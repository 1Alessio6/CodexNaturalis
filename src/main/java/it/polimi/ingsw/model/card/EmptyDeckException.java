package it.polimi.ingsw.model.card;

/**
 * Empty Deck Exception is thrown when the deck of cards is empty.
 */
public class EmptyDeckException extends Exception {
    /**
     * Constructs an <code>EmptyDeckException</code> with no detail message.
     */
    public EmptyDeckException()  {
        super("Empty deck");
    }

    /**
     * Constructs an <code>EmptyDeckException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public EmptyDeckException(String message) {
        super(message);
    }
}
