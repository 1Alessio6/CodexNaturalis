package it.polimi.ingsw.model.card;

/**
 * Empty Deck Exception is thrown when the deck of cards is empty.
 */
public class EmptyDeckException extends Exception {
    public EmptyDeckException()  {
        super("Empty deck");
    }

    public EmptyDeckException(String message) {
        super(message);
    }
}
