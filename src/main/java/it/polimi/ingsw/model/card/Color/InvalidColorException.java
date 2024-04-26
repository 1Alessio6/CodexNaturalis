package it.polimi.ingsw.model.card.Color;

public class InvalidColorException extends Exception {
    public InvalidColorException() {
        super("Invalid color");
    }

    public InvalidColorException(String message) {
        super(message);
    }
}
