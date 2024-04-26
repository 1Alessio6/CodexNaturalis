package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
todo: in order to follow the design pattern this should be an abstract class with a list of listeners. we have to decide
 if we prefer to maintain the pattern and change into an abstract class or if we add the list of VirtualView only in the rmi and socket server
 */


public interface VirtualServer extends Remote {

    public void connect(VirtualView client) throws RemoteException; //possible to add username as a parameter

}
