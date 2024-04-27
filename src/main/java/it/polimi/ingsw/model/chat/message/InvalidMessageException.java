package it.polimi.ingsw.model.chat.message;

public class InvalidMessageException extends Exception {
    public InvalidMessageException() {
        super("Invalid message");
    }

    public InvalidMessageException(String message) {
        super(message);
    }
}
