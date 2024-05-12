package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameRequest;
import it.polimi.ingsw.controller.InvalidIdForDrawingException;
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
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.NotAvailableUsername;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface VirtualServer extends Remote {
    /**
     * Connect to server
     * @param client
     * @throws RemoteException
     */
    void connect(VirtualView client, String username) throws RemoteException, FullLobbyException, InvalidUsernameException;

    void placeStarter(String username, Side side) throws InvalidPlayerActionException, InvalidGamePhaseException, RemoteException;

    void chooseColor(String username, PlayerColor color) throws NonexistentPlayerException, InvalidColorException, InvalidPlayerActionException, InvalidGamePhaseException, RemoteException;

    void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException, InvalidGamePhaseException, RemoteException;

    void placeCard(String username, int frontId, int backId, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, RemoteException, InvalidCardIdException;

    void draw(String username, int idToDraw) throws InvalidPlayerActionException, EmptyDeckException, InvalidGamePhaseException, RemoteException, InvalidIdForDrawingException, InvalidFaceUpCardException;

    void sendMessage(Message message) throws InvalidMessageException, RemoteException;

    void setPlayersNumber(int playersNumber) throws RemoteException, InvalidPlayersNumberException;

    void disconnect(String username) throws InvalidUsernameException, RemoteException;

    // notify about the existence.
    void sendPing(String username) throws RemoteException;
}
