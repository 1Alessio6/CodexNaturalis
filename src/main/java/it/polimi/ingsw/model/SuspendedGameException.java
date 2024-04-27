package it.polimi.ingsw.model;

public class SuspendedGameException extends Exception {
    public SuspendedGameException() {
        super("Suspended game");
    }

    public SuspendedGameException(String message) {
        super(message);
    }
}
