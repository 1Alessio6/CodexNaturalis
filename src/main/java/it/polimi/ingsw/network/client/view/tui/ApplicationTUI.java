package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientApplication;
import it.polimi.ingsw.network.client.ClientMain;
import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.client.controller.ClientController;

import java.rmi.RemoteException;

/**
 * ApplicationTUI runs the client in a text-based user interface
 */
public class ApplicationTUI implements ClientApplication {
    /**
     * {@inheritDoc}
     */
    @Override
    public void run(String typeConnection, String clientIp) throws UnReachableServerException, RemoteException {
        Client client = ClientMain.createClient(typeConnection, clientIp);
        ClientController clientController = new ClientController(client);
        ClientTUI clientTUI = new ClientTUI(clientController);
        clientTUI.runView();
    }
}
