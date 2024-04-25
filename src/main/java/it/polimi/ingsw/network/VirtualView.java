package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
This class defines a "virtual client" and represent the means with which the client interacts with the server. Follow the design
 */
public interface VirtualView extends Remote {

    //todo check if the exceptions should be handled inside the method
    void reportError(String details) throws RemoteException;

}
