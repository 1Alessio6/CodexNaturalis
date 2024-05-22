package it.polimi.ingsw.network.heartbeat;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeartBeatHandler extends Remote {
    void handleUnresponsiveness(String unresponsiveListener) throws RemoteException;
}
