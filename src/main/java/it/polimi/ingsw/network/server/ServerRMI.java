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
    public void notifyColor(PlayerColor color, String username) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showColor(color, username);
        }
    }



    @Override
    public void notifyRemainingColor(Set<PlayerColor> remainingColors) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showRemainingColor(remainingColors);
        }
    }

    @Override
    public void notifyUpdatePlaygroundArea(Position position, ClientTile tile, String username) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdatePlaygroundArea(position,tile,username);
        }
    }

    @Override
    public void notifyUpdatePoints(int points, String username) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdatePoints(points,username);
        }
    }

    @Override
    public void notifyUpdateResources(Symbol symbol, int totalAmount, String username) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdateResources(symbol,totalAmount,username);
        }
    }

    @Override
    public void notifyRemovePlayerCard(int backID, int frontID, int cardPosition, String Username){
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showRemovePlayerCard(backID,frontID,cardPosition,Username);
        }
    }

    public void notifyAddPlayerCard(int backID, int frontID, int cardPosition, String Username){
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showAddPlayerCard(backID,frontID,cardPosition,Username);
        }
    }
    @Override
    public void notifyUpdateDeck(boolean isEmpty, int backID) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdateDeck(isEmpty,backID);
        }

    }

    @Override
    public void notifyUpdateFaceUpCards(int position, Card card) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdateFaceUpCards(position, card.getFace(Side.BACK).getId(), card.getFace(Side.FRONT).getId());
        }
    }

    @Override
    public void notifyCommonObjectiveCard(int[] commonObjectiveID) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showCommonObjectiveCard(commonObjectiveID);
        }
    }

    @Override
    public void notifyUpdatePlayerObjectiveCard(int[] objectiveId, String username) throws RemoteException {
        connectedClients.get(username).showUpdatePlayerObjectiveCard(objectiveId);
    }

    @Override
    public void notifyPlayerStarterCard(Card starterCard, String username) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showPlayerStarterCard(starterCard.getFace(Side.BACK).getId(), starterCard.getFace(Side.FRONT).getId(), username);
        }
    }

    @Override
    public void notifyUpdateChat(Message message) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdateChat(message);
        }
    }

    @Override
    public void notifyUpdateCurrentPlayer(Player currentPlayer) throws RemoteException {

    }

    @Override
    public void notifyUpdateGamePhase(String gamePhase) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdateGamePhase(gamePhase);
        }
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
