package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.ClientPhase;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
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

    public void updateAfterConnection(ClientGame clientGame);

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

    void showInitialPlayerStatus(ClientPlayer player) throws RemoteException;

    //todo check if there are all the methods to show the starter player information
    //method called only to give the cards the first time, not after the player choice
    //it's the only method that show information to one player per time
    // information needed at the beginning of the game.
    // fixme(readability). to improve readability replace all parameters with a single player.
    // give info about the player
 //   //before board setUp every player has 3 cards representing his first hand
 //   // todo. merge with showBoardSetup
 //   void showFirstPlayersCards(Map<String, List<ClientCard>> firstCardsAssigned);

    void showBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException;

    void showStarterPlacement(String username, int faceId);
    // all clients receive the information, the ones not being username will remove color from their list of available colors.
    void showUpdateColor(PlayerColor color, String username) throws RemoteException;

    void showUpdateObjectiveCard(ClientCard chosenObjective, String username);

    //method to show updated information after a place
    //The two list positions and tiles represent every new available tile added after the last place invocation
    //symbols and total amount represent the new resources added after the last place invocation
    //consider both couples like maps
    void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard ,Position position) throws RemoteException;

    void showUpdateAfterDraw(ClientCard drawnCard, boolean isEmpty, ClientCard newTopDeck, ClientCard newFaceUpCard, ClientCard newTopCard, boolean additionalTurn, String username, int boardPosition) throws RemoteException;

    //method to notify update after a draw

    void showUpdateChat(Message message) throws RemoteException;

    void showUpdateCurrentPlayer(ClientPlayer currentPlayer, ClientPhase phase) throws RemoteException;

    void showUpdateSuspendedGame() throws RemoteException;

    void showWinners(List<String> winners) throws RemoteException;

    public void reportError(String details) throws RemoteException;
}