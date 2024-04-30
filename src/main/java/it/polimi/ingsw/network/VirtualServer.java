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
import it.polimi.ingsw.network.client.ClientTile;

import java.io.StringReader;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

/*
 */


public interface VirtualServer extends Remote, GameRequest {
    public abstract void connect(VirtualView client) throws RemoteException; //possible to add username as a parameter

    public abstract  void connect(VirtualView client, String username) throws RemoteException;

    //notify methods

    //notify methods which update is related to only one player (even if the others can see it too) contains an integer that indicates
    //the index inside the list of listeners of the player involved. It's needed an identification for clients because not all update are equal for all clients

    public void notifyPlayerUsername(String username) throws RemoteException; //possible to add username as a parameter

    public void notifyUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException;

    public void notifyColor(PlayerColor color, String username) throws RemoteException;

    public void notifyRemainingColor(Set<PlayerColor> remainingColor) throws RemoteException;

    //method to notify updated information after a place
    public void notifyUpdatePlaygroundArea(Position position, ClientTile tile, String username) throws RemoteException;

    public void notifyUpdatePoints(int points, String username) throws RemoteException;

    public void notifyUpdateResources(Symbol symbol, int totalAmount, String username) throws RemoteException;

    //method to notify update after a draw

    public void notifyRemovePlayerCard(int backID, int frontID, int cardPosition, String Username);

    public void notifyAddPlayerCard(int backID, int frontID, int cardPosition, String Username);

    //backID show the top of the deck(stack) while isEmpty = false backID != -1, when the deck it's empty there isn't anything on the top and backID will be -1
    public void notifyUpdateDeck(boolean isEmpty, int backID) throws RemoteException;

    public void notifyUpdateFaceUpCards(int position, Card card) throws RemoteException;
    //Face up cards have specific position inside the list, so it's possible to pass only the position which needs to be updated

    void notifyCommonObjectiveCard(int[] commonObjectiveID) throws RemoteException;

    public void notifyUpdatePlayerObjectiveCard(int[] commonObjectiveID, String username) throws RemoteException;
    //it's the only method that show information to one player per time

    public void notifyPlayerStarterCard(Card starterCard, String username) throws RemoteException;

    public void notifyUpdateChat(Message message) throws RemoteException;

    public void notifyUpdateCurrentPlayer(Player currentPlayer) throws RemoteException;

    //broadcast
    public void notifyUpdateGamePhase(String GamePhase) throws RemoteException;

}
