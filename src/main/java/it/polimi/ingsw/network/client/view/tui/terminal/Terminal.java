package it.polimi.ingsw.network.client.view.tui.terminal;

import com.sun.jna.Platform;

/**
 * Terminal contains all functionalities to manage the terminal modes and the ANSI code.
 */
public abstract class Terminal {
    /**
     * It is the instance of the Terminal used to implement a singleton pattern.
     */
    private static Terminal INSTANCE = null;

    /**
     * This is the only way to retrieve a Terminal instance as indicated by the singleton pattern.
     *
     * @return the Terminal instance according to the OS on which the program is running.
     * @throws TerminalException if the terminal emulator which is running the program doesn't support raw mode.
     */
    public static Terminal getInstance() throws TerminalException {
        if (INSTANCE == null) {
            INSTANCE = Platform.isWindows() ? WindowsTerminal.getInstance() : UnixTerminal.getInstance();
        }

        return INSTANCE;
    }


    // Note. arrow keys value are chosen randomly from 1000 because they do not correspond with an unique character automatically mapped to an integer, like '\r'==13;
    // instead they are a combination of characters.

    /**
     * ANSI escape code representing up arrow key.
     */
    public static final int UP_ARROW = 1000;

    /**
     * ANSI escape code representing down arrow key.
     */
    public static final int DOWN_ARROW = 1001;

    /**
     * ANSI escape code representing right arrow key.
     */
    public static final int RIGHT_ARROW = 1002;

    /**
     * ANSI escape code representing left arrow key.
     */
    public static final int LEFT_ARROW = 1003;

    /**
     * ANSI escape code representing ESC key.
     */
    public static final char ESC = '\033';

    /**
     * ANSI escape code representing the ENTER key.
     */
    public static final char ENTER = '\r';

    /**
     * ANSI escape code representing the backspace key.
     */
    public static final char DEL = 127;

    /**
     * Sets the terminal to raw mode.
     *
     * @throws TerminalException if the terminal emulator on which the program is being executed doesn't support
     *                           raw mode.
     */
    public abstract void enableRawMode() throws TerminalException;

    /**
     * Restores the terminal to its original configuration.
     *
     * @throws TerminalException if the system call which tries to restore the original configuration fails.
     */
    public abstract void disableRawMode() throws TerminalException;
}
