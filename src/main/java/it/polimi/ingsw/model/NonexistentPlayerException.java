package it.polimi.ingsw.model;

/**
 * Nonexistent Player Exception is thrown when the searched player doesn't exist.
 */
public class NonexistentPlayerException extends Exception{
    public NonexistentPlayerException(){super("The player doesn't exist");}
    public NonexistentPlayerException(String message){super(message);}
}
