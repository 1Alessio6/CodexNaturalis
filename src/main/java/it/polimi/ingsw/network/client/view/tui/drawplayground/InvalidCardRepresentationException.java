package it.polimi.ingsw.network.client.view.tui.drawplayground;

public class InvalidCardRepresentationException extends UndrawablePlaygroundException {
    public InvalidCardRepresentationException(int width, int height) {
        super("Invalid Card representation respect to the playground dimension: width=" + width + " height=" + height);
    }
}
