package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;

import java.rmi.RemoteException;

public abstract class Client {
    protected ClientController controller;
    protected View clientView;
    protected VirtualServer server;

    protected abstract void connect(String ip, Integer port) throws UnReachableServerException;

    public Client(String ip, int port) throws UnReachableServerException {
        connect(ip, port);
        this.controller = new ClientController(server);
    }

    public void addView(View view) {
        clientView = view;
    }

    public ClientController getController() {
        return controller;
    }

    public abstract void runView() throws RemoteException;
}