package it.polimi.ingsw.model;

public class InexistentPlayer extends Exception{
    public InexistentPlayer(){
        super("The player doesn't exist");
    }
}
