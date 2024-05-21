package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerRMI implements VirtualServer{
    private final Controller myController;
    private final static String SERVER_NAME = "ServerRmi";
    private final Object lockOnClientsNetworkStatus;
    private final Map<String, Timer> timerForActiveClients;
    private final Map<String, VirtualView> activeClients;
    private static final int DELAY_FOR_CLIENTS_RESPONSE = 10000;

    public static String getServerName() {
        return SERVER_NAME;
    }

    public ServerRMI() {
        this.myController = new Controller();
        timerForActiveClients = new HashMap<>();
        activeClients = new HashMap<>();
        lockOnClientsNetworkStatus = new Object();
    }

    public static void main(String[] args) {
        ServerRMI myServer = new ServerRMI();
        VirtualServer stub;
        int port = Integer.parseInt(args[0]);

        try {
            stub = (VirtualServer) UnicastRemoteObject.exportObject(myServer, port);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            registry.bind(SERVER_NAME, stub);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("ServerRMI ready");
    }

    @Override
    public void connect(VirtualView client, String username) throws RemoteException {
        boolean accepted = myController.handleConnection(username, client);
        if (accepted) {
            synchronized (lockOnClientsNetworkStatus) {
                timerForActiveClients.put(username, new Timer());
                activeClients.put(username, client);
            }
        }
    }

    private void handleDisconnection(String disconnectedUser) {
        myController.handleDisconnection(disconnectedUser);
        synchronized (lockOnClientsNetworkStatus) {
            timerForActiveClients.remove(disconnectedUser);
            activeClients.remove(disconnectedUser);
        }
        System.out.println("User " + disconnectedUser + " left the server :(");
    }

    @Override
    public void disconnect(String username) throws RemoteException {
        handleDisconnection(username);
    }

    @Override
    public void receivePing(String username) throws RemoteException {
        synchronized (lockOnClientsNetworkStatus) {
            try {
                activeClients.get(username).notifyStillActive("server");
                Timer clientTimer = timerForActiveClients.get(username);
                clientTimer.cancel();
                clientTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handleDisconnection(username);
                    }
                }, DELAY_FOR_CLIENTS_RESPONSE);
            } catch (RemoteException e) {
                activeClients.remove(username);
                timerForActiveClients.get(username).cancel();
                timerForActiveClients.remove(username);
            }
        }
    }

    @Override
    public void placeStarter(String username, Side side) throws RemoteException {
        myController.placeStarter(username, side);
    }

    @Override
    public void chooseColor(String username, PlayerColor color) throws RemoteException {
        myController.chooseColor(username, color);
    }

    @Override
    public void placeObjectiveCard(String username, int chosenObjective) throws RemoteException {
        myController.placeObjectiveCard(username, chosenObjective);
    }

    @Override
    public void placeCard(String username, int frontId, int backId, Side side, Position position) throws RemoteException {
        myController.placeCard(username, frontId, backId, side, position);
    }

    @Override
    public void draw(String username, int idToDraw) throws RemoteException {
        myController.draw(username, idToDraw);
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        myController.sendMessage(message);
    }

    @Override
    public void setPlayersNumber(String username, int playersNumber) throws RemoteException {
        myController.setPlayersNumber(username, playersNumber);
    }

}
