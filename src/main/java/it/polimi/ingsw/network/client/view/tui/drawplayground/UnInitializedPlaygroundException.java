package it.polimi.ingsw.network.client.view.tui.drawplayground;
/**
 * Uninitialized Playground Exception is thrown when an attempt is made for printing the playground, but the
 * playground has not been initialized
 */
public class UnInitializedPlaygroundException extends UndrawablePlaygroundException {
    /**
     * Constructs an <code>UnInitializedPlaygroundException</code> with no detail message
     */
    public UnInitializedPlaygroundException() {
        super("Playground has not been initialized");
    }
}
