package it.polimi.ingsw.network.client.view.drawplayground;

public class UnInitializedPlaygroundException extends Exception {
    public UnInitializedPlaygroundException() {
        super("Playground has not been initialized");
    }
}
