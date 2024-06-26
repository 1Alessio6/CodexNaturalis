package it.polimi.ingsw.network;
import it.polimi.ingsw.network.heartbeat.HeartBeatListener;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * This class defines a "virtual server" and represents the means by which the server interacts with the client.
 */
public interface VirtualServer extends Remote, HeartBeatListener, GameActions {
    /**
     * Connects to the server.
     *
     * @param clientHandler the representation of the client.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void connect(VirtualView clientHandler, String username) throws RemoteException;

    /**
     * Disconnects the <code>username</code> from the game.
     *
     * @param username of the client to be disconnected.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void disconnect(String username) throws RemoteException;
}
