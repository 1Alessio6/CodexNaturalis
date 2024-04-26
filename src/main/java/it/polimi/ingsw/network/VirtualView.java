package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Symbol;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/*
This class defines a "virtual client" and represent the means with which the client interacts with the server.
Follow the observer design pattern
 */
public interface VirtualView extends Remote {


    //method to show players base information
    void showPlayerUsername(String username) throws RemoteException;

    void showUpdatePlayerStatus(boolean isConnected) throws RemoteException;

    void showColor(Color color) throws RemoteException;

    void showRemainingColor(Set<Color> remainingColor) throws RemoteException;





}
