package it.polimi.ingsw.controller;

public class InvalidIdForDrawingException extends Exception {
    public InvalidIdForDrawingException() {
        super("Invalid id for drawing");
    }
}
