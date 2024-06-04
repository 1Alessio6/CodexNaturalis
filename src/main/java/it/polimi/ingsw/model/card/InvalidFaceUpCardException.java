package it.polimi.ingsw.model.card;

/**
 * Invalid FaceUp Card Exception is thrown when the provided id doesn't match with any face up card.
 */
public class InvalidFaceUpCardException extends Exception {
    public InvalidFaceUpCardException(String message)  {
        super(message);
    }
}
