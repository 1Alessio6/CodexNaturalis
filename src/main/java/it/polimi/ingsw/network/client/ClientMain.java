package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.rmi.ClientRMI;
import it.polimi.ingsw.network.client.socket.ClientSocket;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.client.view.gui.ClientGUI;
import it.polimi.ingsw.network.client.view.tui.ClientTUI;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.stream.Stream;

public class ClientMain {
    private static final String OPTION_RMI = "rmi";
    private static final String OPTION_SOCKET = "socket";
    private static final String OPTION_TUI = "tui";
    private static final String OPTION_GUI = "gui";

    private static Client createClient(String host, int port, String typeConnection, String typeView) throws UnReachableServerException {
        Client client;
        if (typeConnection.equals(OPTION_RMI)) {
            client = new ClientRMI(host, port);
        } else {
            client = new ClientSocket(host, port);
        }
        View view;
        if (typeView.equals(OPTION_GUI)) {
            view = new ClientGUI();
        } else {
            view = new ClientTUI(client.getController());
        }
        client.addView(view);
        return client;
    }

    public static void main(String[] args) {
        String typeConnection = OPTION_SOCKET;
        String typeView = OPTION_GUI;
        String host = "localhost";
        int port = 1234;

        // todo(command-line parser)
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
                        port = Integer.parseInt(args[i+1]);
                    }

                    if (args[i].equalsIgnoreCase("--host")) {
                        host = args[i+1];
                    }
                }
            } else if (args.length > 6){
                ClientUtil.argsHelper("Too many arguments");
            }

            Client c = createClient(host, port, typeConnection, typeView);
            c.runView();
        } catch (UnReachableServerException | RemoteException | IndexOutOfBoundsException e) {
            ClientUtil.argsHelper(e.getMessage());
            System.exit(1);
        }
    }
}
