package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;

import java.rmi.RemoteException;

/**
 * The Client updates the view content and the information present in the ClientController.
 */
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

    /**
     * Constructs a <code>Client</code> with no parameters provided.
     */
    public Client() {}

//    public void addView(View view) {
//        clientView = view;
//    }
//
//    public void addController(ClientController controller) {
//        this.controller = controller;
//    }

    /**
     * Configures the Client with the <code>controller</code> and the <code>view</code> provided
     *
     * @param controller the representation of the controller
     * @param view       the representation of the view
     */
    public void configure(ClientController controller, View view) {
        this.controller = controller;
        this.clientView = view;
    }

    /**
     * Supplies the instance for the server.
     *
     * @return the instance of the server.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    public abstract VirtualView getInstanceForTheServer() throws RemoteException;

    /**
     * Connects the client to the <code>ip</code> and <code>port</code> provided.
     *
     * @param ip   the ip address.
     * @param port the port number.
     * @throws UnReachableServerException if the server isn't reachable.
     */
    public abstract VirtualServer bindServer(String ip, Integer port) throws UnReachableServerException;

    public ClientController getController() {
        return controller;
    }

    /**
     * Runs the view.
     *
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    public abstract void runView() throws RemoteException;

    /**
     * Closes any resource related to networking
     */
    protected abstract void disconnect();
}