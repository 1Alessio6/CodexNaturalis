package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.card.InvalidCardIdException;
import it.polimi.ingsw.model.card.InvalidFaceUpCardException;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;

import java.rmi.RemoteException;
import java.util.Set;

/**
 * This interface represents all methods/actions that can be invoked from clients
 */
public interface GameRequest {
    /**
     * Places the starter card of the player in the chosen side.
     *
     * @param username the player's username.
     * @param side     the side of the starter card.
     */
    void placeStarter(String username, Side side) throws InvalidPlayerActionException, InvalidGamePhaseException;

    /**
     * Assigns color to the player with <code>username</code>.
     *
     * @param username of the player who have chosen the color.
     * @param color    chosen by the player.
     */
    void chooseColor(String username, PlayerColor color) throws NonexistentPlayerException, InvalidColorException, InvalidPlayerActionException, InvalidGamePhaseException, RemoteException;

    /**
     * Places the secret objective from one of the two available.
     *
     * @param username        of the player.
     * @param chosenObjective the chosen objective.
     */
    void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException, InvalidGamePhaseException;

    /**
     * Places the card on the side and position specified.
     *
     * @param username of the player.
     * @param frontId  of the card's front to place.
     * @param backId   of the card's back to place.
     * @param side     of the card.
     * @param position in the playground.
     */
    void placeCard(String username, int frontId, int backId, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, InvalidCardIdException;

    /**
     * Draws a card from the selected place.
     *
     * @param username the username of the player.
     * @param idToDraw the id representing the deck or any of the face up card positions.
     */
    void draw(String username, int idToDraw) throws InvalidPlayerActionException, EmptyDeckException, InvalidGamePhaseException, InvalidIdForDrawingException, InvalidFaceUpCardException;

    /**
     * Sends message from the author.
     *
     * @param message sent by the author.
     */
    void sendMessage(Message message) throws InvalidMessageException;

    /**
     * Sets the number of players required to start the game,
     * and if the game has not yet started and the number of players has been reached, it starts the game and remove
     * the excess players.
     *
     * @param username      of the first player.
     * @param playersNumber the number of players established by the first player.
     */
    void setPlayersNumber(String username, int playersNumber) throws InvalidPlayersNumberException;
}