package it.polimi.ingsw.network.heartbeat;

import java.rmi.RemoteException;

public interface HeartBeatListener {
    void receivePing(String senderName) throws RemoteException;
}
