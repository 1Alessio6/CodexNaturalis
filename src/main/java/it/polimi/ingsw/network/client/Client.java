package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;

import java.rmi.RemoteException;

/**
 * The Client updates the view content and the information present in the ClientController.
 */
public abstract class Client implements VirtualView {
    protected ClientController controller;
    protected View clientView;
    protected VirtualServer server;
    protected String name;

    /**
     * Connects the client to the <code>ip</code> and <code>port</code> provided.
     *
     * @param ip   the ip address.
     * @param port the port number.
     * @throws UnReachableServerException if the server isn't reachable.
     */
    protected abstract void connect(String ip, Integer port) throws UnReachableServerException;

    /**
     * Constructs the Client using the <code>ip</code> and the <code>port</code> provided.
     *
     * @param ip   the ip address.
     * @param port the port number.
     * @throws UnReachableServerException if the server isn't reachable.
     */
    public Client(String ip, int port) throws UnReachableServerException {
        connect(ip, port);
        this.controller = new ClientController(server);
    }

    /**
     * Adds the <code>view</code> to the Client.
     *
     * @param view the representation of the view.
     */
    public void addView(View view) {
        clientView = view;
    }

    public ClientController getController() {
        return controller;
    }

    /**
     * Runs the view.
     *
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    public abstract void runView() throws RemoteException;
}