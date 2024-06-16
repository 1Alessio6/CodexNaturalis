package it.polimi.ingsw.network.client.view.tui;

/**
 * TUI Exception is thrown when an error occurs during the TUI running
 */
public class TUIException extends Exception {
    public TUIException(ExceptionsTUI exception) {
        super(exception.getMessage());
    }
}
