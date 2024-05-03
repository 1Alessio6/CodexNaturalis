package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRequest;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.ClientPhase;
import it.polimi.ingsw.network.client.ClientTile;

import java.io.StringReader;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 */

//todo use a different interface and remove gameRequest (username not needed)
public interface VirtualServer extends Remote, GameRequest {
    public void connect(VirtualView client) throws RemoteException; //possible to add username as a parameter

    public void connect(VirtualView client, String username) throws RemoteException;

    //notify methods

    //notify methods which update is related to only one player (even if the others can see it too) contains an integer that indicates
    //the index inside the list of listeners of the player involved. It's needed an identification for clients because not all update are equal for all clients

    void notifyPlayerUsername(String username) throws RemoteException; //see if it's needed or if it could be handled with an exception

    void notifyUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException;

    //method called only to give the cards the first time, not after the player choice
    //it's the only method that show information to one player per time
    void notifyUpdatePlayerObjectiveCard(int[] commonObjectiveID, int starterCardID, String username) throws RemoteException;

    void notifyBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException;

    void notifyColor(PlayerColor color, String username) throws RemoteException;

    //method to notify updated information after a place
    //The two list positions and tiles represent every new available tile added after the last place invocation
    //symbols and total amount represent the new resources added after the last place invocation
    //consider both couples like maps
    void notifyUpdateAfterPlace(List<Position> positions, List<ClientTile> tiles, List<Symbol> symbols, int[] totalAmount, int points, String username) throws RemoteException;

    //todo decide if it's useful to mantain a map for goldenFrontRequirements or it's better to use two lists
    //goldenFrontRequirements it's an empty Map field if the front drawn isn't golden
    void notifyUpdateAfterDraw(int newBackID, int newFrontID, Map<Symbol, Integer> goldenFrontRequirements, int cardHandPosition, boolean isEmpty, int newDeckBackID, int deckType, int newFrontFaceUp, int newBackFaceUp, int positionFaceUp, String Username) throws RemoteException;

    //method to notify update after a draw

    void notifyUpdateChat(Message message) throws RemoteException;

    void notifyUpdateCurrentPlayer(Player currentPlayer, ClientPhase phase) throws RemoteException;

}
