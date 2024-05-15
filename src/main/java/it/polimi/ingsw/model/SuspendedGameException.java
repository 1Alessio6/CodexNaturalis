package it.polimi.ingsw.model;

public class SuspendedGameException extends Exception {
    public SuspendedGameException() {
        super("You can't play now, the game is suspended");
    }

    public SuspendedGameException(String message) {
        super(message);
    }
}
