package it.polimi.ingsw.network.client;

import java.rmi.RemoteException;

public interface ClientApplication {
    void run(String typeConnection, String host, String port) throws UnReachableServerException, RemoteException;
}
