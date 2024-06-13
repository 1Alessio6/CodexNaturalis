package it.polimi.ingsw.network.client;

public class UnReachableServerException extends Exception {
    public UnReachableServerException() {
        super("Unreachable server");
    }

    public UnReachableServerException(String message) {
        super(message);
    }
}
