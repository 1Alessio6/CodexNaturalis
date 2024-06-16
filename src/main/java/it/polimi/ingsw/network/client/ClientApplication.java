package it.polimi.ingsw.network.client;

import java.rmi.RemoteException;

public interface ClientApplication {

    /**
     * Method used to run the client in a certain <code>typeConnection</code>, <code>host</code> and <code>port</code>
     *
     * @param typeConnection the type of connection can be RMI or Socket.
     * @param host           the name of the host to connect to.
     * @param port           the port number.
     * @throws UnReachableServerException if the server isn't reachable.
     * @throws RemoteException            in the event of an error occurring during the execution of a remote method.
     */
    void run(String typeConnection, String host, String port) throws UnReachableServerException, RemoteException;
}
