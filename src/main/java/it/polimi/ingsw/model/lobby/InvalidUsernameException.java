package it.polimi.ingsw.model.lobby;

public class InvalidUsernameException extends Exception{
    public InvalidUsernameException() {
        super("This username has already been taken, please try with another one");
    }
}
