package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Deck;
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

    //method to show updated information after a place

    //it's more efficient to pass only the new added tile and position in order to avoid sending the whole map to the client every time
    void showUpdatePlaygroundArea(Position position, Tile tile) throws RemoteException;

    void showUpdatePoints(int points) throws RemoteException;

    void showUpdateResources(Symbol symbol, int totalAmount) throws RemoteException;

    //method to show update after a draw

    void showUpdatePlayerCards(List<Card> newCards);

    void showUpdateDeck(boolean isEmpty); //the client (player) can't see the updates of a deck till the deck is empty

    void showUpdateFaceUpCards(int position, Card card);
    //Face up cards have specific position inside the list, so it's possible to pass only the position which needs to be updated

    void reportError(String details) throws RemoteException;

    //todo check if showUpdatedTimer is required or two synchronized timer are used for both server and client



}
