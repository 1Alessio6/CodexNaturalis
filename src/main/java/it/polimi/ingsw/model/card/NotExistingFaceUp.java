package it.polimi.ingsw.model.card;

public class NotExistingFaceUp extends Exception{
    public NotExistingFaceUp()  {
        super("Empty face up position");
    }

    public NotExistingFaceUp(String message) {
        super(message);
    }
}
