package it.polimi.ingsw.model;

public class NonexistentPlayerException extends Exception{
    public NonexistentPlayerException(){super("The player doesn't exist");}
}
