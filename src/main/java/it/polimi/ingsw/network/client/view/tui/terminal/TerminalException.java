package it.polimi.ingsw.network.client.view.tui.terminal;

/**
 * TerminalException is thrown whenever an attempt to change the terminal mode fails.
 */
public class TerminalException extends Exception {
    /**
     * Constructor of the class.
     * Initializes the exception message.
     *
     * @param message is the exception message.
     */
    public TerminalException(String message) {
        super(message);
    }
}
