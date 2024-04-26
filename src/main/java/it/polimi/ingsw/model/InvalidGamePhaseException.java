package it.polimi.ingsw.model;

public class InvalidGamePhaseException extends Exception {
    public InvalidGamePhaseException() {
        super("Invalid game phase");
    }

    public InvalidGamePhaseException(String message) {
        super(message);
    }
}
