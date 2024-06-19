package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.controller.InvalidIdForDrawingException;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.card.NotExistingFaceUp;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.RemoteException;

/**
 * This interface represents all methods/actions that can be invoked from clients
 */
public interface ClientActions {
    /**
     * Handles the connection of the <code>client</code>.
     *
     * @param username the client's name.
     * @throws InvalidUsernameException if the username in question has already been taken.
     * @throws RemoteException          in the event of an error occurring during the execution of a remote method.
     * @throws FullLobbyException       if the lobby is full.
     */
    void connect(String username) throws InvalidUsernameException, RemoteException, FullLobbyException;

    /**
     * Places the <code>cardHandPosition</code> on the <code>selectedSide</code> and <code>position</code> specified.
     *
     * @param cardHandPosition the position of the card in the player's hand.
     * @param selectedSide     of the card.
     * @param position         in the playground at which it will be placed.
     * @throws Playground.UnavailablePositionException if the position isn't empty or isn't contained in the player's playground.
     * @throws Playground.NotEnoughResourcesException  if the resources needed to place the card aren't enough.
     * @throws InvalidGamePhaseException               if the game phase doesn't allow placing cards.
     * @throws SuspendedGameException                  if the game is suspended.
     * @throws RemoteException                         in the event of an error occurring during the execution of a remote method.
     */
    void placeCard(int cardHandPosition, Side selectedSide, Position position) throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, RemoteException;

    /**
     * Draws a card from the <code>IdToDraw</code>.
     *
     * @param IdToDraw the id representing the deck or any of the face up cards positions.
     * @throws InvalidGamePhaseException    if the game doesn't allow to draw cards.
     * @throws EmptyDeckException           in the event that the selected deck is empty.
     * @throws NotExistingFaceUp            if the face up slot is empty.
     * @throws SuspendedGameException       if the game is suspended.
     * @throws RemoteException              in the event of an error occurring during the execution of a remote method.
     * @throws InvalidIdForDrawingException if the id of the card isn't valid.
     */
    void draw(int IdToDraw) throws  InvalidGamePhaseException, EmptyDeckException, NotExistingFaceUp, SuspendedGameException, RemoteException, InvalidIdForDrawingException;

    /**
     * Places the starter card of the player in the chosen <code>side</code>.
     *
     * @param side the side of the starter card.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException if the game phase doesn't allow placing cards.
     */
    void placeStarter(Side side) throws SuspendedGameException, RemoteException, InvalidGamePhaseException;

    /**
     * Assigns <code>color</code> to the <code>mainPlayerUsername</code>.
     *
     * @param color chosen by the <code>mainPlayerUsername</code>.
     * @throws InvalidColorException     if the color has already been selected.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException if the game phase doesn't allow to choose the <code>color</code>.
     */
    void chooseColor(PlayerColor color) throws InvalidColorException, SuspendedGameException, RemoteException, InvalidGamePhaseException;

    /**
     * Places the <code>chosenObjective</code> from one of the two available.
     *
     * @param chosenObjective the chosen objective.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException if the game phase doesn't allow to place the objective card.
     */
    void placeObjectiveCard(int chosenObjective) throws SuspendedGameException, RemoteException, InvalidGamePhaseException;

    /**
     * Sends message from the <code>mainPlayerUsername</code>.
     *
     * @param message sent by the <code>mainPlayerUsername</code>.
     * @throws InvalidMessageException if the author doesn't match the author or the recipient doesn't exist
     * @throws RemoteException         in the event of an error occurring during the execution of a remote method.
     */
    void sendMessage(Message message) throws InvalidMessageException, RemoteException;

    /**
     * Sets the number of players allowed in the lobby.
     * @param playersNumber the number of players of the game.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     * @throws InvalidPlayersNumberException if the <code>size</code> is greater than 4 or less than 2
     */
    void setPlayersNumber(int playersNumber) throws RemoteException, InvalidPlayersNumberException;

    /**
     * Disconnects the <code>username</code> from the game.
     *
     * @param username of the player.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    void disconnect(String username) throws RemoteException;

}
