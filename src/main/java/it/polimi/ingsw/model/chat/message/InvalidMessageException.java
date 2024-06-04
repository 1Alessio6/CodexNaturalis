package it.polimi.ingsw.model.chat.message;

/**
 * Invalid Message Exception is thrown when the author doesn't match the sender or the recipient is an invalid username
 */
public class InvalidMessageException extends Exception {
    public InvalidMessageException() {
        super("Invalid message");
    }

    public InvalidMessageException(String message) {
        super(message);
    }
}
