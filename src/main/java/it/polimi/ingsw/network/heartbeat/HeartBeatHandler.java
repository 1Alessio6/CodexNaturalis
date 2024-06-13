package it.polimi.ingsw.network.heartbeat;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeartBeatHandler extends Remote {

    /**
     * Handles the unresponsiveness of clients.
     *
     * @param unresponsiveListener the unresponsiveness client.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void handleUnresponsiveness(String unresponsiveListener) throws RemoteException;
}
