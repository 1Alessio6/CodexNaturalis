package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientTile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/*
This class defines a "virtual client" and represent the means with which the client interacts with the server.
Follow the observer design pattern
 */
public interface VirtualView extends Remote {

    public String getUsername() throws RemoteException; //todo: check if it's possible to remove and put a map <username,client> into the server

    //method to show players base information
    public void showPlayerUsername(String username) throws RemoteException;

    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException;

    public void showColor(PlayerColor color, String username) throws RemoteException;

    public void showRemainingColor(Set<PlayerColor> remainingColor) throws RemoteException;

    //method to show updated information after a place

    //it's more efficient to pass only the new added tile and position in order to avoid sending the whole map to the client every time
    public void showUpdatePlaygroundArea(Position position, ClientTile tile, String username) throws RemoteException;

    public void showUpdatePoints(int points, String username) throws RemoteException;

    public void showUpdateResources(Symbol symbol, int totalAmount, String username) throws RemoteException;

    //method to show update after a draw

    public void showRemovePlayerCard(int backID, int frontID, int cardPosition, String Username);

    public void showAddPlayerCard(int backID, int frontID, int cardPosition, String Username);

    public void showUpdateDeck(boolean isEmpty, int backID) throws RemoteException; //the client (player) can't see the updates of a deck till the deck is empty

    public void showUpdateFaceUpCards(int position, Card card) throws RemoteException;
    //Face up cards have specific position inside the list, so it's possible to pass only the position which needs to be updated

    public void showCommonObjectiveCard(int[] commonObjective) throws RemoteException;

    public void showUpdatePlayerObjectiveCard(int[] privateObjective) throws RemoteException;
    //it's the only method that show information to one player per time

    public void showPlayerStarterCard(int starterBackID, int starterFrontID, String username) throws RemoteException;

    public void showUpdateChat(Message message) throws RemoteException;

    public void showUpdateCurrentPlayer(Player currentPlayer) throws RemoteException;

    public void showUpdateGamePhase(String GamePhase) throws RemoteException;

    public void reportError(String details) throws RemoteException;

    //todo check if showUpdatedTimer is required or two synchronized timer are used for both server and client


}
