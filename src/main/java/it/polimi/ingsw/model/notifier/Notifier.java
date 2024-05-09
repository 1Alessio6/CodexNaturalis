package it.polimi.ingsw.model.notifier;

import it.polimi.ingsw.network.VirtualView;

import java.rmi.RemoteException;

public interface Notifier<ReceiverType> {
    public void sendUpdate(ReceiverType receiver) throws RemoteException;
}
