package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientApplication;
import it.polimi.ingsw.network.client.ClientMain;
import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.tui.ClientTUI;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;

public class ApplicationTUI implements ClientApplication {
    @Override
    public void run(String typeConnection) throws UnReachableServerException, RemoteException {
        Client client = ClientMain.createClient(typeConnection);
        ClientController clientController = new ClientController(client);
        ClientTUI clientTUI = new ClientTUI(clientController);
        clientTUI.runView();
    }
}
