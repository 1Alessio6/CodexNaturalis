package it.polimi.ingsw.model.lobby;

public class InvalidUsernameException extends Exception{
    public InvalidUsernameException() {
        super("Invalid username");
    }

    public InvalidUsernameException(String message) {
        super(message);
    }
}
