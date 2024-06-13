package it.polimi.ingsw.network.heartbeat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeartBeatListener extends Remote{
    /**
     * Receives pings from clients
     *
     * @param ping the ping from the client
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void receivePing(HeartBeatMessage ping) throws RemoteException;
}
