package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamephase.GamePhase;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.heartbeat.HeartBeatListener;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/**
 * This class defines a "virtual client" and represents the means by which the client interacts with the server.
 * Follows the observer design pattern.
 */
public interface VirtualView extends HeartBeatListener, Remote {
    /**
     * Method used to show a welcome message to the creator and allow him/her to set the lobby size.
     *
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void updateCreator() throws RemoteException;

    /**
     * Method used to show the crash of the lobby through a message in output.
     *
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void updateAfterLobbyCrash() throws RemoteException;

    /**
     * Method used to show the update from the current game to <code>clientGame</code>.
     *
     * @param clientGame the clientGame to which the game is to be updated.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void updateAfterConnection(ClientGame clientGame) throws RemoteException;

    /**
     * Method used to update the lobby with the <code>usernames</code> of the players.
     *
     * @param usernames of the players.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException;

    /**
     * Method used to notify an exceeding player about his/her status.
     *
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdateExceedingPlayer() throws RemoteException;

    /**
     * Method used to show basic player information.
     * It notifies about the player disconnections, and also when a new player connects.
     *
     * @param isConnected player status.
     * @param username    of the player.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException;

    /**
     * Method used to show the chosen color.
     * All clients receive the information, the ones that aren't the <code>username</code>, will remove
     * <code>color</code> from their list of available colors.
     *
     * @param color    the color chosen by the player.
     * @param username the username of the player.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdateColor(PlayerColor color, String username) throws RemoteException;

    /**
     * Method used to show the objective card.
     *
     * @param chosenObjective the chosen objective.
     * @param username        of the player.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) throws RemoteException;

    /**
     * Method used to show updated information after a place.
     * The two list positions and tiles represent every new available tile added after the last place invocation
     * symbols and total amount represents the new resources added after the last place invocation
     * consider both couples like maps.
     *
     * @param positionToCornerCovered a map with the covered corners.
     * @param newAvailablePositions   a list with every new available tile added after the last placement.
     * @param newResources            the new resources added after the last placement.
     * @param points                  the points present after the placement.
     * @param username                the username of the player.
     * @param placedCard              the card that has been placed.
     * @param placedSide              the side of the card that has been placed.
     * @param position                the position of the card in the playground.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide , Position position) throws RemoteException;

    /**
     * Method used to notify the update after a draw.
     *
     * @param drawnCard     the drawn card.
     * @param newTopDeck    the new card that replaces the previous card that was present in the deck.
     * @param newFaceUpCard the new face up card that replaces the previous one.
     * @param username      the username of the player who performs the drawing.
     * @param boardPosition from which the card was selected, 4 for golden deck, 5 for resource deck and 0,1,2 or 3 for
     *                      face up cards.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException;

    /**
     * Method used to show the updated chat.
     *
     * @param message the new message to be displayed.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdateChat(Message message) throws RemoteException;

    /**
     * Method used to show the current player's turn.
     *
     * @param currentPlayerIdx the index of the current player.
     * @param phase            the phase in which the current player is.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException;

    /**
     * Method used to show the update of the state of the game.
     *
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showUpdateGameState() throws RemoteException;

    /**
     * Method used to show the winners of the game.
     *
     * @param winners the winners of the game.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void showWinners(List<String> winners) throws RemoteException;

    /**
     * Method used to report error <code>details</code>.
     *
     * @param details the details to be reported.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void reportError(String details) throws RemoteException;

    /**
     * Method used to connect the player or an output with the <code>details</code>, depending on whether the
     * player is <code>accepted</code> or not.
     *
     * @param accepted boolean indicating whether the player is accepted or not.
     * @param username the player's username.
     * @param details  the error details.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void resultOfLogin(boolean accepted, String username, String details) throws RemoteException;
}