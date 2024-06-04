package it.polimi.ingsw.model;

/**
 * Invalid Game Phase Exception is thrown when an attempt is made to perform an action that is not allowed at a given phase of the game
 */
public class InvalidGamePhaseException extends Exception {
    public InvalidGamePhaseException() {
        super("Invalid game phase");
    }

    public InvalidGamePhaseException(String message) {
        super(message);
    }
}
