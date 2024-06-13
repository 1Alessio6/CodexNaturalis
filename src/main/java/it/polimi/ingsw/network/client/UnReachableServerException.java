package it.polimi.ingsw.network.client;

/**
 * Unreachable Server Exception is thrown when the server isn't accessible.
 */
public class UnReachableServerException extends Exception {
    public UnReachableServerException() {
        super("Unreachable server");
    }
}
