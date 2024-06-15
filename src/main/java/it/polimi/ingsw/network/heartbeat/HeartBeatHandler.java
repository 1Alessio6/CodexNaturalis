package it.polimi.ingsw.network.heartbeat;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HeartBeatHandler {
    void handleUnresponsiveness(String unresponsiveListener) ;
}
