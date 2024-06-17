package it.polimi.ingsw.model.lobby;

/**
 * Invalid Username Exception is thrown when the player tries to enter a game using a name that is not present at the
 * beginning of the game or enters with a name that is already registered in the game
 */
public class InvalidUsernameException extends Exception{
    /**
     * Constructs an <code>InvalidUsernameException</code> with no detail message
     */
    public InvalidUsernameException() {
        super("This username has already been taken, please try with another one");
    }

    /**
     * Constructs an <code>InvalidUsernameException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public InvalidUsernameException(String message) {
        super(message);
    }
}
