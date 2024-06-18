package it.polimi.ingsw.network.client.view.tui;

/**
 * TUI Exception is thrown when an error occurs during the TUI running
 */
public class TUIException extends Exception {
    /**
     * Constructs a <code>TUIException</code> with the <code>exception</code> provided
     *
     * @param exception the detail exception
     */
    public TUIException(ExceptionsTUI exception) {
        super(exception.getMessage());
    }
}
