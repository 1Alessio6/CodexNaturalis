package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.rmi.ClientRMI;
import it.polimi.ingsw.network.client.socket.ClientSocket;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.client.view.gui.ClientGUI;
import it.polimi.ingsw.network.client.view.tui.ClientTUI;

import java.rmi.RemoteException;

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
        String typeConnection = OPTION_RMI;
        String typeView = OPTION_TUI;

        // todo(command-line parser)

        try {
            Client c = createClient(args[0], Integer.parseInt(args[1]), typeConnection, typeView);
            c.runView();
        } catch (UnReachableServerException | RemoteException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
