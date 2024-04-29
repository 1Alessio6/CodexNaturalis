package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServerRMI implements VirtualServer {
    private final static int PORT = 1234;
    private final Controller myController;

    List<VirtualView> connectedClients;

    public ServerRMI() {
        this.connectedClients = new ArrayList<>();
        this.myController = new Controller();
    }

    @Override
    public void connect(VirtualView client) throws RemoteException {

    }

    @Override
    public void connect(VirtualView client, String username) throws RemoteException {
    }

    @Override
    public void notifyPlayerUsername(String username, Integer clientIndex) throws RemoteException {
        this.connectedClients.get(clientIndex).showPlayerUsername(username);
    }

    @Override
    public void notifyUpdatePlayerStatus(boolean isConnected, Integer clientIndex) throws RemoteException {
        this.connectedClients.get(clientIndex).showUpdatePlayerStatus(isConnected);
    }

    @Override
    public void notifyColor(PlayerColor color, Integer clientIndex) throws RemoteException {
        this.connectedClients.get(clientIndex).showColor(color);
    }

    @Override
    public void notifyRemainingColor(Set<PlayerColor> remainingColors) throws RemoteException {
        for (VirtualView virtualView : this.connectedClients) {
            virtualView.showRemainingColor(remainingColors);
        }
    }

    @Override
    public void notifyUpdatePlaygroundArea(Position position, ClientTile tile, String username) throws RemoteException {
        for (VirtualView virtualView : this.connectedClients) {
            virtualView.showUpdatePlaygroundArea(position,tile,username);
        }
    }

    @Override
    public void notifyUpdatePoints(int points, Integer clientIndex) throws RemoteException {
        this.connectedClients.get(clientIndex).showUpdatePoints(points);
    }

    @Override
    public void notifyUpdateResources(Symbol symbol, int totalAmount, Integer clientIndex) throws RemoteException {
        this.connectedClients.get(clientIndex).showUpdateResources(symbol, totalAmount);
    }

    @Override
    public void notifyUpdatePlayerCards(List<Card> newCards, Integer clientIndex) throws RemoteException {
        this.connectedClients.get(clientIndex).showUpdatePlayerCards(newCards);
    }

    @Override
    public void notifyUpdateDeck(boolean isEmpty) throws RemoteException {
        for (VirtualView virtualView : this.connectedClients) {
            virtualView.showUpdateDeck(isEmpty);
        }

    }

    @Override
    public void notifyUpdateFaceUpCards(int position, Card card) throws RemoteException {
        for (VirtualView virtualView : this.connectedClients) {
            virtualView.showUpdateFaceUpCards(position, card);
        }
    }

    @Override
    public void notifyCommonObjectiveCard(List<ObjectiveCard> commonObjective) throws RemoteException {
        for (VirtualView virtualView : this.connectedClients) {
            virtualView.showCommonObjectiveCard(commonObjective);
        }
    }

    @Override
    public void notifyUpdatePlayerObjectiveCard(List<ObjectiveCard> privateObjective, Integer clientIndex) throws RemoteException {
        this.connectedClients.get(clientIndex).showUpdatePlayerObjectiveCard(privateObjective);
    }

    @Override
    public void notifyPlayerStarterCard(Card starterCard, Integer clientIndex) throws RemoteException {
        this.connectedClients.get(clientIndex).showPlayerStarterCard(starterCard);
    }

    @Override
    public void notifyUpdateChat(Message message) throws RemoteException {
        for (VirtualView virtualView : this.connectedClients) {
            virtualView.showUpdateChat(message);
        }
    }

    @Override
    public void notifyUpdateCurrentPlayer(Player currentPlayer) throws RemoteException {

    }

    @Override
    public void notifyUpdateGamePhase(String gamePhase) throws RemoteException {
        for (VirtualView virtualView : this.connectedClients) {
            virtualView.showUpdateGamePhase(gamePhase);
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
