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

    public void notifyPlayerUsername(String username, Integer clientIndex) throws RemoteException; //possible to add username as a parameter

    public void notifyUpdatePlayerStatus(boolean isConnected, Integer clientIndex) throws RemoteException;

    public void notifyColor(PlayerColor color, Integer clientIndex) throws RemoteException;

    public void notifyRemainingColor(Set<PlayerColor> remainingColor) throws RemoteException;

    //method to notify updated information after a place
    //client index indicates which client playground needs to be updated

    public void notifyUpdatePlaygroundArea(Position position, ClientTile tile, String username) throws RemoteException;

    public void notifyUpdatePoints(int points, Integer clientIndex) throws RemoteException;

    public void notifyUpdateResources(Symbol symbol, int totalAmount, Integer clientIndex) throws RemoteException;

    //method to notify update after a draw

    public void notifyUpdatePlayerCards(List<Card> newCards, Integer clientIndex) throws RemoteException;

    public void notifyUpdateDeck(boolean isEmpty) throws RemoteException; //the client (player) can't see the updates of a deck till the deck is empty

    public void notifyUpdateFaceUpCards(int position, Card card) throws RemoteException;
    //Face up cards have specific position inside the list, so it's possible to pass only the position which needs to be updated

    public void notifyCommonObjectiveCard(List<ObjectiveCard> commonObjective) throws RemoteException;

    public void notifyUpdatePlayerObjectiveCard(List<ObjectiveCard> privateObjective, Integer clientIndex) throws RemoteException;
    //it's the only method that show information to one player per time

    public void notifyPlayerStarterCard(Card starterCard, Integer clientIndex) throws RemoteException;

    public void notifyUpdateChat(Message message) throws RemoteException;

    public void notifyUpdateCurrentPlayer(Player currentPlayer) throws RemoteException;

    public void notifyUpdateGamePhase(String GamePhase) throws RemoteException;

}
