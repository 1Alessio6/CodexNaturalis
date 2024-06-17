package it.polimi.ingsw.model.card;

/**
 * Invalid Card ID Exception is thrown when the provided id doesn't match with any existing card.
 */
public class InvalidCardIdException extends Exception {
    /**
     * Constructs an <code>InvalidCardIdException</code> with no detail message.
     */
    public InvalidCardIdException() {
        super("Illegal card id");
    }
}
