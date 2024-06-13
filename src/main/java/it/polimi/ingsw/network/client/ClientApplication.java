package it.polimi.ingsw.network.client;

import java.rmi.RemoteException;

public interface ClientApplication {
    void run(String typeConnection) throws UnReachableServerException, RemoteException;
}
