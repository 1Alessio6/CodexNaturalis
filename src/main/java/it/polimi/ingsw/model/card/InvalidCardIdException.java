package it.polimi.ingsw.model.card;

public class InvalidCardIdException extends Exception {
    public InvalidCardIdException() {
        super("Illegal card id");
    }
}
