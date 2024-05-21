package it.polimi.ingsw.network.client.view.drawplayground;

public class InvalidCardRepresentationException extends Exception {
    public InvalidCardRepresentationException(int width, int height) {
        super("Invalid Card representation respect to the playground dimension: width=" + width + " height=" + height);
    }
}
