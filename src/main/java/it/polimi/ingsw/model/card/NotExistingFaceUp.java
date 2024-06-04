package it.polimi.ingsw.model.card;

/**
 * Not Existing Face Up is thrown when in a given position, no face up card is found
 */
public class NotExistingFaceUp extends Exception{
    public NotExistingFaceUp()  {
        super("Empty face up position");
    }

    public NotExistingFaceUp(String message) {
        super(message);
    }
}
