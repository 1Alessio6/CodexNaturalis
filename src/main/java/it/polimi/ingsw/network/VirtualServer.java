package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRequest;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.NotAvailableUsername;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface VirtualServer extends Remote, GameRequest {
    /**
     * Connect to server
     * @param client
     * @throws RemoteException
     */
    void connect(VirtualView client, String username) throws RemoteException, FullLobbyException, InvalidUsernameException;

    void disconnect(String username) throws InvalidUsernameException, RemoteException;

    // notify about the existence.
    void sendPing(String username);
}
