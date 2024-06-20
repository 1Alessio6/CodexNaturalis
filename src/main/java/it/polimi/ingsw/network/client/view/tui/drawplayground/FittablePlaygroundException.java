package it.polimi.ingsw.network.client.view.tui.drawplayground;
/**
 * Fittable Playground Exception is thrown when the player attempts to move into an already represented playground
 * offset
 */
public class FittablePlaygroundException extends UndrawablePlaygroundException {
    /**
     * Constructs a <code>FittablePlaygroundException</code> with no detail message
     */
    public FittablePlaygroundException() {
        super("All playground is already represented: no need to move!");
    }
}
