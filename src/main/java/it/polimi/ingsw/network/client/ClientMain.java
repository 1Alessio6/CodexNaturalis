package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.rmi.ClientRMI;
import it.polimi.ingsw.network.client.socket.ClientSocket;
import it.polimi.ingsw.network.client.view.gui.ApplicationGUI;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public class ClientMain {
    private static final String OPTION_RMI = "rmi";
    private static final String OPTION_SOCKET = "socket";
    private static final String OPTION_TUI = "tui";
    private static final String OPTION_GUI = "gui";

    public static Client createClient(List<String> params) throws UnReachableServerException {
        Client client;
        String typeConnection = params.get(0);
        String host = params.get(1);
        Integer port = Integer.parseInt(params.get(2));
        if (typeConnection.equals(OPTION_RMI)) {
            client = new ClientRMI(host, port);
        } else {
            client = new ClientSocket(host, port);
        }
        return client;
    }

    public static void main(String[] args) {
        String typeConnection = OPTION_SOCKET;
        String typeView = OPTION_GUI;
        String host = "localhost";
        String port = "1234";

        try {
            // max 4 args: rmi tui port <int>
            if (args.length > 0 && args.length <= 6) {
                // override stuff
                if (Arrays.stream(args).anyMatch("--rmi"::equalsIgnoreCase)) {
                    typeConnection = OPTION_RMI;
                }
                if (Arrays.stream(args).anyMatch("--tui"::equalsIgnoreCase)) {
                    typeView = OPTION_TUI;
                }
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equalsIgnoreCase("--port")) {
                        port = args[i + 1];
                    }

                    if (args[i].equalsIgnoreCase("--host")) {
                        host = args[i + 1];
                    }
                }
            } else if (args.length > 6) {
                ClientUtil.argsHelper("Too many arguments");
            }
            ClientApplication application;
            if (typeView.equals("tui")) {
                application = new ApplicationTUI();
            } else {
                application = new ApplicationGUI();
            }
            application.run(typeConnection, host, port);
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
