package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;

import java.rmi.RemoteException;
import java.util.Set;

/**
 * This interface represents all methods/actions that can be invoked from clients
 */
public interface GameRequest {
    void placeStarter(String username, Side side) throws InvalidPlayerActionException, InvalidGamePhaseException;

    void chooseColor(String username, PlayerColor color) throws NonexistentPlayerException, InvalidColorException, InvalidPlayerActionException, InvalidGamePhaseException, RemoteException;

    void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException, InvalidGamePhaseException;

    void placeCard(String username, int frontId, int backId, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException;

    void draw(String username, int idToDraw) throws InvalidPlayerActionException, EmptyDeckException, InvalidGamePhaseException;

    void sendMessage(Message message) throws InvalidMessageException;

    void setPlayersNumber(int playersNumber);
}