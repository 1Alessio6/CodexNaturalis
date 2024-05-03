package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
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
import it.polimi.ingsw.model.lobby.AlreadyInLobbyException;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.ClientPhase;
import it.polimi.ingsw.network.client.ClientTile;

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
    public void connect(VirtualView client) throws RemoteException {

    }

    @Override
    public void connect(VirtualView client, String username) throws RemoteException {
    }

    @Override
    public void notifyPlayerUsername(String username) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showPlayerUsername(username);
        }
    }

    @Override
    public void notifyUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdatePlayerStatus(isConnected,username);
        }
    }

    @Override
    public void notifyUpdatePlayerObjectiveCard(int[] commonObjectiveID, int starterCardID, String username) throws RemoteException {

    }

    @Override
    public void notifyBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException {

    }

    @Override
    public void notifyColor(PlayerColor color, String username) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdateColor(color, username);
        }
    }

    @Override
    public void notifyUpdateAfterPlace(List<Position> positions, List<ClientTile> tiles, List<Symbol> symbols, int[] totalAmount, int points, String username) throws RemoteException {

    }

    @Override
    public void notifyUpdateAfterDraw(int newBackID, int newFrontID, Map<Symbol, Integer> goldenFrontRequirements, int cardHandPosition, boolean isEmpty, int newDeckBackID, int deckType, int newFrontFaceUp, int newBackFaceUp, int positionFaceUp, String Username) throws RemoteException {

    }

    @Override
    public void notifyUpdateChat(Message message) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdateChat(message);
        }
    }

    @Override
    public void notifyUpdateCurrentPlayer(Player currentPlayer, ClientPhase phase) throws RemoteException {

    }

    @Override
    public boolean joinLobby(String username) throws FullLobbyException, AlreadyInLobbyException {
        return false;
    }

    @Override
    public void joinGame(String username) {
        this.myController.joinGame(username);
    }

    @Override
    public boolean leaveLobby(String username) {
        return false;
    }

    @Override
    public boolean leaveGame(String username) {
        return false;
    }

    @Override
    public void placeStarter(String username, Side side) throws InvalidPlayerActionException, InvalidGamePhaseException {

    }

    @Override
    public List<PlayerColor> assignColor(String username, PlayerColor color) throws NonexistentPlayerException, InvalidColorException, InvalidPlayerActionException, InvalidGamePhaseException {
        return List.of();
    }

    @Override
    public void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException, InvalidGamePhaseException {

    }

    @Override
    public int placeCard(String username, int frontId, int backId, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException {
        return 0;
    }

    @Override
    public void draw(String username, int idToDraw) throws InvalidPlayerActionException, EmptyDeckException, InvalidGamePhaseException {

    }

    @Override
    public String getCurrentPlayer() {
        return "";
    }

    @Override
    public void skipTurn() {

    }

    @Override
    public void sendMessage(String author, Message message) throws InvalidMessageException {

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
