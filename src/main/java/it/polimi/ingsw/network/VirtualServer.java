package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRequest;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.player.NotAvailableUsername;
import it.polimi.ingsw.network.client.model.ClientPhase;
import it.polimi.ingsw.network.client.model.ClientPlayer;
import it.polimi.ingsw.network.client.model.ClientTile;
import it.polimi.ingsw.network.client.model.card.ClientCard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/*
 */

//todo use a different interface and remove gameRequest (username not needed)
public interface VirtualServer extends Remote, GameRequest {
    /**
     * Connect to server
     * @param client
     * @throws RemoteException
     */
    void connect(VirtualView client, String username) throws RemoteException, NotAvailableUsername;
    boolean disconnect(String username);


    //notify methods

    //notify methods which update is related to only one player (even if the others can see it too) contains an integer that indicates
    //the index inside the list of listeners of the player involved. It's needed an identification for clients because not all update are equal for all clients

    void notifyPlayerUsername(String username) throws RemoteException; //see if it's needed or if it could be handled with an exception

    void notifyUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException;

    //method called only to give the cards the first time, not after the player choice
    //it's the only method that show information to one player per time

    void notifyUpdatePlayerObjectiveCard(int[] commonObjectiveID, ClientCard starterCard, String username) throws RemoteException;

    void notifyBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException;

    void notifyColor(PlayerColor color, String username) throws RemoteException;

    //method to notify updated information after a place
    //The two list positions and tiles represent every new available tile added after the last place invocation
    //symbols and total amount represent the new resources added after the last place invocation
    //consider both couples like maps
    //todo decide if it's useful to mantain a map for goldenFrontRequirements or it's better to use two lists

    //method to notify update after a draw

    void notifyUpdateAfterPlace(Map<Position, ClientTile> newAvailablePosition, Map<Symbol, Integer> newResources, int points, String username) throws RemoteException;

    void notifyUpdateAfterDraw(ClientCard card,
                               int cardHandPosition,
                               boolean isEmpty,
                               int newDeckBackID,
                               int deckType,
                               int newFrontFaceUp,
                               int newBackFaceUp,
                               int positionFaceUp,
                               String username) throws RemoteException;

    void notifyUpdateChat(Message message) throws RemoteException;

    void notifyUpdateCurrentPlayer(ClientPlayer currentPlayer, ClientPhase phase) throws RemoteException;

}
