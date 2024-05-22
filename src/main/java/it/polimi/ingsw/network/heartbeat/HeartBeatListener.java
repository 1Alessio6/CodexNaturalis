package it.polimi.ingsw.network.heartbeat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeartBeatListener extends Remote{
    void receivePing(HeartBeatMessage ping) throws RemoteException;
}
