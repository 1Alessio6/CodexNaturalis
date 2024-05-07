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
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.NotAvailableUsername;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientPhase;
import it.polimi.ingsw.network.client.model.ClientPlayer;
import it.polimi.ingsw.network.client.model.ClientTile;
import it.polimi.ingsw.network.client.model.card.ClientCard;

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
    public void connect(VirtualView client, String username) throws NotAvailableUsername {
        // TODO: change connect behaviour

        /*
        try {
            String clientUsername = String.valueOf(connectedClients.entrySet().stream()
                    .filter(a -> a.getValue().equals(client)) // get the one and only calling client
                    .map(Map.Entry::getKey) // get its associated username
                    .findFirst()); // get as string: if there isn't, it means that there isn't such client

            myController.handleConnection(clientUsername);
        } catch (FullLobbyException | AlreadyInLobbyException e) {
            throw new RuntimeException(e);
        }
        */
    }

    @Override
    public boolean disconnect(String username) {
        return myController.handleDisconnection(username);
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
            connectedClients.get(user).showUpdatePlayerStatus(isConnected, username);
        }
    }

    @Override
    public void notifyUpdatePlayerObjectiveCard(int[] commonObjectiveID, ClientCard starterCard, String username) throws RemoteException {
        connectedClients.get(username).showUpdatePlayerObjectiveCard(commonObjectiveID, starterCard, username);
    }

    @Override
    public void notifyBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException {
        for (VirtualView virtualView : connectedClients.values()) {
            virtualView.showBoardSetUp(commonObjectiveID, topBackID, topGoldenBackID, faceUpCards);
        }
    }

    @Override
    public void notifyColor(PlayerColor color, String username) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdateColor(color, username);
        }
    }

    @Override
    public void notifyUpdateAfterPlace(Map<Position, ClientTile> newAvailablePosition, Map<Symbol, Integer> newResources, int points, String username) throws RemoteException {
        for (VirtualView virtualView : this.connectedClients.values()) {
            virtualView.showUpdateAfterPlace(newAvailablePosition, newResources, points, username);
        }
    }

    @Override
    public void notifyUpdateAfterDraw(ClientCard card,
                                      int cardHandPosition,
                                      boolean isEmpty,
                                      int newDeckBackID,
                                      int deckType,
                                      int newFrontFaceUp,
                                      int newBackFaceUp,
                                      int positionFaceUp,
                                      String username) throws RemoteException {
        for (VirtualView virtualView : this.connectedClients.values()) {
            virtualView.showUpdateAfterDraw(card,
                        cardHandPosition,
                        isEmpty,
                        newDeckBackID,
                        deckType,
                        newFrontFaceUp,
                        newBackFaceUp,
                        positionFaceUp,
                        username);
        }
    }

    @Override
    public void notifyUpdateChat(Message message) throws RemoteException {
        for (String user : connectedClients.keySet()) {
            connectedClients.get(user).showUpdateChat(message);
        }
    }

    @Override
    public void notifyUpdateCurrentPlayer(ClientPlayer currentPlayer, ClientPhase phase) throws RemoteException {
        for (VirtualView virtualView : connectedClients.values()) {
            virtualView.showUpdateCurrentPlayer(currentPlayer, phase);
        }
    }

    @Override
    public void placeStarter(String username, Side side) throws InvalidPlayerActionException, InvalidGamePhaseException {
        myController.placeStarter(username, side);
    }

    @Override
    public Set<PlayerColor> chooseColor(String username, PlayerColor color) {
        Set<PlayerColor> remainingColors = Set.of();

        try {
            remainingColors = myController.chooseColor(username, color);

            notifyColor(color, username);
        } catch (NonexistentPlayerException | InvalidColorException | InvalidPlayerActionException |
                 InvalidGamePhaseException | RemoteException e) {
            e.printStackTrace();
        }

        return remainingColors;
    }

    @Override
    public void placeObjectiveCard(String username, int chosenObjective) {
        try {
            myController.placeObjectiveCard(username, chosenObjective);

            // TODO: notify
        } catch (InvalidPlayerActionException | InvalidGamePhaseException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public int placeCard(String username, int frontId, int backId, Side side, Position position) {
        try {
            myController.placeCard(username, frontId, backId, side, position);

            // todo: notify after place
        } catch (InvalidPlayerActionException | Playground.UnavailablePositionException |
                 Playground.NotEnoughResourcesException | InvalidGamePhaseException | SuspendedGameException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    @Override
    public boolean draw(String username, int idToDraw) {
        try {
            myController.draw(username, idToDraw);

            // todo : notify
        } catch (InvalidPlayerActionException | EmptyDeckException | InvalidGamePhaseException e) {
            throw new RuntimeException(e);
        }


        return false;
    }

    @Override
    public void sendMessage(String author, Message message) {
        try {
            myController.sendMessage(author, message);

            notifyUpdateChat(message);
        } catch (RemoteException | InvalidMessageException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPlayersNumber(int playersNumber) {
        myController.setPlayersNumber(playersNumber);
        // todo: need notify
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
