package it.polimi.ingsw.network.heartbeat;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeartBeatHandler {
    /**
     * Handles the unresponsiveness of the end point being tracked.
     *
     * @param unresponsiveListener the unresponsiveness client.
     */
    void handleUnresponsiveness(String unresponsiveListener) ;
}
