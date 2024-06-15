package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;

import java.rmi.RemoteException;

public abstract class Client implements VirtualView, HeartBeatHandler {
    protected ClientController controller;
    protected View clientView;

    //public Client(String ip, int port) throws UnReachableServerException {
    //    connect(ip, port);
    //    this.controller = new ClientController(server);
    //}
    //public Client(View clientView, ClientController controller) {
    //    this.clientView = clientView;
    //    this.controller = controller;
    //}

    public Client() {}

//    public void addView(View view) {
//        clientView = view;
//    }
//
//    public void addController(ClientController controller) {
//        this.controller = controller;
//    }

    public void configure(ClientController controller, View view) {
        this.controller = controller;
        this.clientView = view;
    }

    public abstract VirtualView getInstanceForTheServer() throws RemoteException;

    public abstract VirtualServer bindServer(String ip, Integer port) throws UnReachableServerException;

    public ClientController getController() {
        return controller;
    }

    public abstract void runView() throws RemoteException;
}