package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;

import java.rmi.RemoteException;

public abstract class Client implements HeartBeatHandler {
    protected ClientController controller;
    protected View clientView;
    protected VirtualServer server;

    protected abstract VirtualServer connect(String ip, Integer port);

    public Client(String ip, int port) throws UnReachableServerException {
        server = connect(ip, port);
        if (server == null) {
            throw new UnReachableServerException();
        }
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