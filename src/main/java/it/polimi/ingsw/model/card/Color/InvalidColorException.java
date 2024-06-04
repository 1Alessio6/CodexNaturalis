package it.polimi.ingsw.model.card.Color;

/**
 * Invalid Color Exception is thrown when the player attempts to select a color that has already been selected
 */
public class InvalidColorException extends Exception {
    public InvalidColorException() {
        super("Invalid color");
    }

    public InvalidColorException(String message) {
        super(message);
    }
}
