package it.polimi.ingsw.model.lobby;

/**
 * Invalid Username Exception is thrown when the player tries to enter a game using a name that is not present at the
 * beginning of the game or enters with a name that is already registered in the game
 */
public class InvalidUsernameException extends Exception{
    public InvalidUsernameException() {
        super("This username has already been taken, please try with another one");
    }

    public InvalidUsernameException(String message) {
        super(message);
    }
}
