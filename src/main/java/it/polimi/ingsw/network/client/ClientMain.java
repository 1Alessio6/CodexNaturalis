package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.rmi.ClientRMI;
import it.polimi.ingsw.network.client.socket.ClientSocket;
import it.polimi.ingsw.network.client.view.gui.ApplicationGUI;
import it.polimi.ingsw.network.client.view.tui.ApplicationTUI;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
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
    public static Client createClient(String typeConnection, String clientIp) {
        Client client;
        if (typeConnection.equals(OPTION_RMI)) {
            client = new ClientRMI(clientIp);
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
        String clientIp = "";
        try {
            int maxArgs = 3; // <connectionType> + <client_ip> + <viewType>
            if (args.length > 0 && args.length <= maxArgs) {
                if (args[0].equals(OPTION_RMI)) {
                    typeConnection = OPTION_RMI;
                } else {
                    typeConnection = OPTION_SOCKET;
                }

                clientIp = args[1];

                if (args[2].equals("tui")) {
                    typeView = OPTION_TUI;
                } else {
                    typeView = OPTION_GUI;
                }

                if (typeView.equals(OPTION_TUI)) {
                    try {
                        System.setErr(new PrintStream(new FileOutputStream("log.txt",true)));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

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
            application.run(typeConnection, clientIp);
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
