package it.polimi.ingsw.model;

/**
 * Nonexistent Player Exception is thrown when the searched player doesn't exist.
 */
public class NonexistentPlayerException extends Exception{
    /**
     * Constructs a <code>NonexistentPlayerException</code> with no detail message
     */
    public NonexistentPlayerException(){super("The player doesn't exist");}

    /**
     * Constructs an <code>NonexistentPlayerException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public NonexistentPlayerException(String message){super(message);}
}
