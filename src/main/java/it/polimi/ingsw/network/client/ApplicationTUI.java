package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.view.tui.ClientTUI;

import java.rmi.RemoteException;
import java.util.Arrays;

public class ApplicationTUI implements ClientApplication {
    @Override
    public void run(String typeConnection, String host, String port) throws UnReachableServerException, RemoteException {
        Client c = ClientMain.createClient(Arrays.asList(typeConnection, host, port));
        ClientTUI clientTUI = new ClientTUI(c.getController());
        c.addView(clientTUI);
        c.runView();
    }
}
