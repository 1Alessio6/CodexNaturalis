package it.polimi.ingsw.network.client.view.tui.drawplayground;

/**
 * UndrawablePlaygroundException is thrown when an error occurs during the playground representation design.
 * Particularly, when the playground is fully displayed, the dimensions of the card are invalid
 * or have an invalid representation in relation to the playground dimension and the playground has not been initialized.
 */
public class UndrawablePlaygroundException extends Exception {
    /**
     * Constructs an <code>UndrawablePlaygroundException</code> with the <code>message</code> provided
     *
     * @param message the detail message
     */
    public UndrawablePlaygroundException(String message){
        super(message);
    }
}
