package it.polimi.ingsw.network.client.view.tui.drawplayground;

public class UnInitializedPlaygroundException extends UndrawablePlaygroundException {
    public UnInitializedPlaygroundException() {
        super("Playground has not been initialized");
    }
}
