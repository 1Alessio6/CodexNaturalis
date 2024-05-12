package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.InvalidIdForDrawingException;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerRMI implements VirtualServer {
    private final static int PORT = 1234;
    private final Controller myController;

    Map<String, VirtualView> connectedClients;

    public ServerRMI() {
        this.connectedClients = new HashMap<>();
        this.myController = new Controller();
    }

    @Override
    public void connect(VirtualView client, String username) throws RemoteException, FullLobbyException, InvalidUsernameException {
        myController.handleConnection(username, client);
    }


    @Override
    public void disconnect(String username) throws InvalidUsernameException, RemoteException {
        myController.handleDisconnection(username);
    }

    @Override
    public void sendPing(String username) {

    }

    @Override
    public void placeStarter(String username, Side side) throws InvalidPlayerActionException, InvalidGamePhaseException {
        myController.placeStarter(username, side);
    }

    @Override
    public void chooseColor(String username, PlayerColor color) {
        try {
            myController.chooseColor(username, color);
        } catch (NonexistentPlayerException | InvalidColorException | InvalidPlayerActionException |
                 InvalidGamePhaseException | RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException, InvalidGamePhaseException {
        myController.placeObjectiveCard(username, chosenObjective);
    }

    @Override
    public void placeCard(String username, int frontId, int backId, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, InvalidCardIdException {
        myController.placeCard(username, frontId, backId, side, position);
    }

    @Override
    public void draw(String username, int idToDraw) throws InvalidPlayerActionException, InvalidIdForDrawingException, EmptyDeckException, InvalidGamePhaseException, InvalidFaceUpCardException {
        myController.draw(username, idToDraw);
    }

    @Override
    public void sendMessage(Message message) throws InvalidMessageException {
        myController.sendMessage(message);
    }

    @Override
    public void setPlayersNumber(int playersNumber) throws InvalidPlayersNumberException {
        myController.setPlayersNumber(playersNumber);
    }

    public static void main(String[] args) {
        ServerRMI myServer = new ServerRMI();
        VirtualServer stub = null;

        try {
            stub = (VirtualServer) UnicastRemoteObject.exportObject(myServer, PORT);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            registry.bind("VirtualServer", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("ServerRMI ready");

    }
}
