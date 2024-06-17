package it.polimi.ingsw.model.card;

/**
 * Invalid FaceUp Card Exception is thrown when the provided id doesn't match with any face up card.
 */
public class InvalidFaceUpCardException extends Exception {
    /**
     * Constructs an <code>InvalidFaceUpCardException</code> with the <code>message</code> provided.
     *
     * @param message the detail message
     */
    public InvalidFaceUpCardException(String message)  {
        super(message);
    }
}
