package it.polimi.ingsw.network.client;

/**
 * Unreachable Server Exception is thrown when the server isn't accessible.
 */
public class UnReachableServerException extends Exception {
    /**
     * Constructs an <code>UnReachableServerException</code> with no detail message.
     */
    public UnReachableServerException() {
        super("Unreachable server");
    }

    /**
     * Constructs an <code>UnReachableServerException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public UnReachableServerException(String message) {
        super(message);
    }
}
