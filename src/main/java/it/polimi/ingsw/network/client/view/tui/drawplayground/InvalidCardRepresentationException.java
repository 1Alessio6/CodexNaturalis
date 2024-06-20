package it.polimi.ingsw.network.client.view.tui.drawplayground;

/**
 * Invalid Card Representation Exception is thrown when the card has an invalid representation respect to the playground
 * dimension
 */
public class InvalidCardRepresentationException extends UndrawablePlaygroundException {
    /**
     * Constructs an <code>InvalidCardRepresentationException</code> with the <code>width</code>
     * and the <code>height</code> provided
     *
     * @param width  the width
     * @param height the height
     */
    public InvalidCardRepresentationException(int width, int height) {
        super("Invalid Card representation respect to the playground dimension: width=" + width + " height=" + height);
    }
}
