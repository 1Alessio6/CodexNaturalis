package it.polimi.ingsw.network.heartbeat;

import java.rmi.RemoteException;

public interface PingSender {
    void sendPing(String senderName) throws RemoteException;
}
