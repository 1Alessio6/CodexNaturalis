package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientCard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/*
This class defines a "virtual client" and represent the means with which the client interacts with the server.
Follow the observer design pattern
 */
public interface VirtualView extends Remote {
    public void updateCreator() throws RemoteException;

    public void updateAfterLobbyCrash() throws RemoteException;

    public void updateAfterConnection(ClientGame clientGame) throws RemoteException;

    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException;

    // game related updates

    // game status: beginning of any connection

    // selected Starter

    // selected color

    // place objective card

    // result of normal place

    // result of draw

    // message sent

    // ping from server

    //method to show players base information
    // notify about the player disconnection
    //use when a new player is connected
    void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException;

    void showBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException;

    // all clients receive the information, the ones not being username will remove color from their list of available colors.
    void showUpdateColor(PlayerColor color, String username) throws RemoteException;

    void showUpdateObjectiveCard(ClientCard chosenObjective, String username) throws RemoteException;

    // todo. add parameter in after draw and place to notify which player has triggered the events.
    //method to show updated information after a place
    //The two list positions and tiles represent every new available tile added after the last place invocation
    //symbols and total amount represent the new resources added after the last place invocation
    //consider both couples like maps
    void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide , Position position) throws RemoteException;

    void showUpdateAfterDraw(ClientCard drawnCard, ClientCard newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException;

    //method to notify update after a draw

    void showUpdateChat(Message message) throws RemoteException;

    void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException;

    void showUpdateSuspendedGame() throws RemoteException;

    void showWinners(List<String> winners) throws RemoteException;

    public void reportError(String details) throws RemoteException;
}