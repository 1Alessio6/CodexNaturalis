package it.polimi.ingsw.network.client.view.tui.drawplayground;

public class FittablePlaygroundException extends UndrawablePlaygroundException {
    public FittablePlaygroundException() {
        super("All playground is already represented: no need to move!");
    }
}
