package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface defines all the game actions available to a player.
 */
public interface GameActions extends Remote {
    /**
     * Places the starter.
     *
     * @param username the username of the player.
     * @param side     of the card.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void placeStarter(String username, Side side) throws RemoteException;

    /**
     * Assigns the chosen <code>color</code> to the<code>username</code>.
     *
     * @param username the username of the player.
     * @param color    chosen by the player.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void chooseColor(String username, PlayerColor color) throws RemoteException;

    /**
     * Places the <code>chosenObjective</code>.
     *
     * @param username        the username of the player who chose the <code>chosenObjective</code>.
     * @param chosenObjective the objective card that has been chosen by the player.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void placeObjectiveCard(String username, int chosenObjective) throws RemoteException;

    /**
     * Places a card with the <code>frontId</code> and <code>backId</code> given in the <code>side</code>
     * and <code>position</code> provided.
     *
     * @param username the username of the player.
     * @param frontId  the identification of the front of the card.
     * @param backId   the identification of the back of the card.
     * @param side     the selected side of the card.
     * @param position the selected position to place the card.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void placeCard(String username, int frontId, int backId, Side side, Position position) throws RemoteException;

    /**
     * Allows the <code>username</code> to draw a card.
     *
     * @param username the username of the player.
     * @param idToDraw the identification of the card to draw.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void draw(String username, int idToDraw) throws RemoteException;

    /**
     * Sends a <code>message</code>.
     *
     * @param message the message to send
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void sendMessage(Message message) throws RemoteException;

    /**
     * Allows the <code>username</code> to choose the <code>playersNumber</code> of the game.
     *
     * @param username      the username of the first player.
     * @param playersNumber the number of players allowed in the game.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void setPlayersNumber(String username, int playersNumber) throws RemoteException;
}
