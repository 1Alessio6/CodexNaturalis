package it.polimi.ingsw.model.chat.message;

/**
 * Invalid Message Exception is thrown when the author doesn't match the sender or the recipient is an invalid username
 */
public class InvalidMessageException extends Exception {
    /**
     * Constructs an <code>InvalidMessageException</code> with no detail message
     */
    public InvalidMessageException() {
        super("Invalid message");
    }

    /**
     * Constructs an <code>InvalidMessageException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public InvalidMessageException(String message) {
        super(message);
    }
}
