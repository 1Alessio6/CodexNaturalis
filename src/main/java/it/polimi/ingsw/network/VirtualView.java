package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientPhase;
import it.polimi.ingsw.network.client.ClientTile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/*
This class defines a "virtual client" and represent the means with which the client interacts with the server.
Follow the observer design pattern
 */
public interface VirtualView extends Remote {

    //method to show players base information
    void showPlayerUsername(String username) throws RemoteException; //see if it's needed or if it could be handled with an exception

    void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException;

    //method called only to give the cards the first time, not after the player choice
    //it's the only method that show information to one player per time
    void showUpdatePlayerObjectiveCard(int[] commonObjectiveID, int starterCardID, String username) throws RemoteException;

    void showBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException;

    void showUpdateColor(PlayerColor color, String username) throws RemoteException;

    //method to show updated information after a place
    //The two list positions and tiles represent every new available tile added after the last place invocation
    //symbols and total amount represent the new resources added after the last place invocation
    //consider both couples like maps
    void showUpdateAfterPlace(List<Position> positions, List<ClientTile> tiles, List<Symbol> symbols, int[] totalAmount, int points, String username) throws RemoteException;

    void showUpdateAfterDraw(int newBackID, int newFrontID, int cardHandPosition, boolean isEmpty, int newDeckBackID, int deckType, int newFrontFaceUp, int newBackFaceUp, int positionFaceUp,String Username) throws RemoteException;

    //method to notify update after a draw

    void showUpdateChat(Message message) throws RemoteException;

    void showUpdateCurrentPlayer(Player currentPlayer, ClientPhase phase) throws RemoteException;

    public void reportError(String details) throws RemoteException;

    //todo check if showUpdatedTimer is required or two synchronized timer are used for both server and client


}
