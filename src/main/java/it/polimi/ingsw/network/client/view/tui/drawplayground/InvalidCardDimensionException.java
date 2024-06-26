package it.polimi.ingsw.network.client.view.tui.drawplayground;

/**
 * Invalid Card Dimension Exception is thrown when the card dimensions provided aren't valid, that's because, the card
 * must have an odd dimension
 */
public class InvalidCardDimensionException extends UndrawablePlaygroundException{
    /**
     * Constructs an <code>InvalidCardDimensionException</code> with no detail message
     */
    public InvalidCardDimensionException() {
        super("Invalid card dimension");
    }
}
