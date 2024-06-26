package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.rmi.ClientRMI;
import it.polimi.ingsw.network.client.socket.ClientSocket;
import it.polimi.ingsw.network.client.view.gui.ApplicationGUI;
import it.polimi.ingsw.network.client.view.tui.ApplicationTUI;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;

import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * ClientMain is the class that starts the client in a GUI/TUI interface and in an RMI/Socket communication
 */
public class ClientMain {
    private static final String OPTION_RMI = "rmi";
    private static final String OPTION_SOCKET = "socket";
    private static final String OPTION_TUI = "tui";
    private static final String OPTION_GUI = "gui";

    /**
     * Creates the client with the <code>typeConnection</code> provided
     *
     * @param typeConnection the type of communication to be used, that is, OPTION_RMI or OPTION_SOCKET
     * @return the client in the <code>typeConnection</code> desired
     */
    public static Client createClient(String typeConnection) {
        Client client;
        if (typeConnection.equals(OPTION_RMI)) {
            client = new ClientRMI();
        } else {
            client = new ClientSocket();
        }
        return client;
    }

    /**
     * Runs the application in the <code>args</code> provided
     *
     * @param args the arguments of the connection,
     *             those are, the <code>typeConnection</code> and the <code>typeView</code>
     */
    public static void main(String[] args) {
        String typeConnection = OPTION_SOCKET;
        String typeView = OPTION_GUI;
        try {
            int maxArgs = 2; // <connectionType> + <viewType>
            // max 4 args: rmi tui
            if (args.length > 0 && args.length <= maxArgs) {
                // override stuff
                if (Arrays.stream(args).anyMatch("--rmi"::equalsIgnoreCase)) {
                    typeConnection = OPTION_RMI;
                }
                if (Arrays.stream(args).anyMatch("--tui"::equalsIgnoreCase)) {
                    typeView = OPTION_TUI;
                }
                //for (int i = 0; i < args.length; i++) {
                //    if (args[i].equalsIgnoreCase("--port")) {
                //        port = args[i + 1];
                //    }

                //    if (args[i].equalsIgnoreCase("--host")) {
                //        host = args[i + 1];
                //    }
                //}
            } else if (args.length > maxArgs) {
                ClientUtil.argsHelper("Too many arguments");
            }
            ClientApplication application;
            if (typeView.equals("tui")) {
                application = new ApplicationTUI();
            } else {
                application = new ApplicationGUI();
            }
            //application.run(typeConnection, host, port);
            application.run(typeConnection);
        } catch (UnReachableServerException | RemoteException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (IndexOutOfBoundsException e) {
            ClientUtil.argsHelper(e.getMessage());
            System.exit(1);
        }
    }
}
