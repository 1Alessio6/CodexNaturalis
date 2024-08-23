package it.polimi.ingsw.network.client.view.tui.terminal;

import com.sun.jna.*;

import java.util.Arrays;

/* Code inspired from https://github.com/marcobehlerjetbrains/text-editor/blob/episode-4/Viewer.java
 * (relative tutorial series: https://www.youtube.com/playlist?list=PLIRBoI92yManB1eHCupZ6iG61qMTA9hWe)
 */

class UnixTerminal extends Terminal {
    private static UnixTerminal INSTANCE = null;

    public static UnixTerminal getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnixTerminal();
        }

        return INSTANCE;
    }

    private static LibC.Termios originalAttributes;

    @Override
    public void enableRawMode() throws TerminalException {
        LibC.Termios termios = Platform.isMac() ? new LibC.MacOSTermios() : new LibC.LinuxTermios();
        int rc = LibC.INSTANCE.tcgetattr(LibC.SYSTEM_OUT_FD, termios);

        if (rc != 0) {
            throw new TerminalException("There was a problem trying to retrieve the original mode (call to tcgetattr)");
        }

        originalAttributes = termios.clone();

        LibC.INSTANCE.cfmakeraw(termios);

        rc = LibC.INSTANCE.tcsetattr(LibC.SYSTEM_OUT_FD, LibC.TCSAFLUSH, termios);
        if (rc != 0) {
            throw new TerminalException("There was a problem trying to set the raw mode (call to tcsetattr)");
        }
    }

    @Override
    public void disableRawMode() throws TerminalException {
        int rc = LibC.INSTANCE.tcsetattr(LibC.SYSTEM_OUT_FD, LibC.TCSAFLUSH, originalAttributes);
        if (rc != 0) {
            throw new TerminalException("There was a problem trying to set the original mode");
        }
    }

    interface LibC extends Library {

        int SYSTEM_OUT_FD = 0;
        int ISIG = 1, ICANON = 2, ECHO = 10, TCSAFLUSH = 2,
                IXON = 2000, ICRNL = 400, IEXTEN = 100000, OPOST = 1, VMIN = 6, VTIME = 5, TIOCGWINSZ = Platform.isMac() ? 0x40087468 : 0x5413;

        // we're loading the C standard library for POSIX systems
        LibC INSTANCE = Native.load("c", LibC.class);

        @Structure.FieldOrder(value = {"ws_row", "ws_col", "ws_xpixel", "ws_ypixel"})
        class Winsize extends Structure {
            public short ws_row, ws_col, ws_xpixel, ws_ypixel;
        }

        interface Termios {
            Termios clone();
        }

        @Structure.FieldOrder(value = {"c_iflag", "c_oflag", "c_cflag", "c_lflag", "c_cc"})
        class LinuxTermios extends Structure implements Termios {
            public int c_iflag, c_oflag, c_cflag, c_lflag;

            public byte[] c_cc = new byte[19];

            public LinuxTermios() {
            }

            @Override
            public Termios clone() {
                LinuxTermios copy = new LinuxTermios();
                copy.c_iflag = c_iflag;
                copy.c_oflag = c_oflag;
                copy.c_cflag = c_cflag;
                copy.c_lflag = c_lflag;
                copy.c_cc = c_cc.clone();
                return copy;
            }

            @Override
            public String toString() {
                return "Termios{" +
                        "c_iflag=" + c_iflag +
                        ", c_oflag=" + c_oflag +
                        ", c_cflag=" + c_cflag +
                        ", c_lflag=" + c_lflag +
                        ", c_cc=" + Arrays.toString(c_cc) +
                        '}';
            }
        }

        @Structure.FieldOrder(value = {"c_iflag", "c_oflag", "c_cflag", "c_lflag", "c_cc"})
        class MacOSTermios extends Structure implements Termios {
            public long c_iflag, c_oflag, c_cflag, c_lflag;

            public byte[] c_cc = new byte[19];

            public MacOSTermios() {
            }

            @Override
            public MacOSTermios clone() {
                MacOSTermios copy = new MacOSTermios();
                copy.c_iflag = c_iflag;
                copy.c_oflag = c_oflag;
                copy.c_cflag = c_cflag;
                copy.c_lflag = c_lflag;
                copy.c_cc = c_cc.clone();
                return copy;
            }

            @Override
            public String toString() {
                return "Termios{" +
                        "c_iflag=" + c_iflag +
                        ", c_oflag=" + c_oflag +
                        ", c_cflag=" + c_cflag +
                        ", c_lflag=" + c_lflag +
                        ", c_cc=" + Arrays.toString(c_cc) +
                        '}';
            }
        }

        int tcgetattr(int fd, Termios termios);

        int tcsetattr(int fd, int optional_actions,
                      Termios termios);

        /**
         * Sets the terminal to raw mode enabling the flags as specified in  <a href="https://man7.org/linux/man-pages/man3/termios.3.html">https://man7.org/linux/man-pages/man3/termios.3.html</a>.
         *
         * @param termios_p is a pointer to a termios structure.
         */
        void cfmakeraw(Termios termios_p);
    }

}
