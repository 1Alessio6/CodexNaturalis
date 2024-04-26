package it.polimi.ingsw.model.player;

/**
 * Invalid Player Action Exception  is thrown when the player tries to perform an action that is not allowed
 */
public class InvalidPlayerActionException extends Exception {
    public InvalidPlayerActionException()  {
        super("Invalid player action");
    }

    public InvalidPlayerActionException(String message) {
        super(message);
    }
}
