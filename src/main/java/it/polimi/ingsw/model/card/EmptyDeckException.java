package it.polimi.ingsw.model.card;

/**
 * Empty Deck Exception is thrown when the deck of cards is empty.
 */
public class EmptyDeckException extends Exception {
    EmptyDeckException(String message) {
        super(message);
    }
}
