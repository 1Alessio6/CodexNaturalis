package it.polimi.ingsw.model.card;

/**
 * Not Existing Face Up is thrown when in a given position, no face up card is found
 */
public class NotExistingFaceUp extends Exception{
    /**
     * Constructs a <code>NotExistingFaceUp</code> with no detail message.
     */
    public NotExistingFaceUp()  {
        super("Empty face up position");
    }

    /**
     * Constructs a <code>NotExistingFaceUp</code> with the <code>message</code> provided.
     *
     * @param message the detail message
     */
    public NotExistingFaceUp(String message) {
        super(message);
    }
}
