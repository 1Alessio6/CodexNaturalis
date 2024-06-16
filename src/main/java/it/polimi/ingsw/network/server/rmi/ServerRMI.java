package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.heartbeat.HeartBeat;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * ServerRMI notifies the controller of changes made by the player in the view when RMI communication is used.
 */
public class ServerRMI implements VirtualServer {
    private final Controller myController;
    private final static String SERVER_NAME = "ServerRmi";
    private final Object lockOnClientsNetworkStatus;
    private final Map<String, HeartBeat> activeClients;

    public static String getServerName() {
        return SERVER_NAME;
    }

    /**
     * Constructs the ServerRMI
     */
    public ServerRMI() {
        this.myController = new Controller();
        activeClients = new HashMap<>();
        lockOnClientsNetworkStatus = new Object();
    }

    public static void main(String[] args) {
        ServerRMI myServer = new ServerRMI();
        VirtualServer stub = null;
        int port = Integer.parseInt(args[0]);

        try {
            stub = (VirtualServer) UnicastRemoteObject.exportObject(myServer, port);
        } catch (RemoteException e) {
            System.err.println("Export failed");
            System.exit(1);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            System.err.println("Registry cannot be exported");
            System.exit(1);
        }

        try {
            registry.bind(SERVER_NAME, stub);
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Failed to bind the server");
            System.exit(1);
        }

        System.out.println("ServerRMI ready");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(VirtualView client, String username) throws RemoteException {
        System.out.println("Received connection from " + username);
        boolean accepted = myController.handleConnection(username, client);
        if (accepted) {
            // System.err.println("User " + username + " has been accepted");
            synchronized (lockOnClientsNetworkStatus) {
                HeartBeat heartBeat = new HeartBeat(this, "server", client, username);
                activeClients.put(username, heartBeat);
                heartBeat.startHeartBeat();
            }
        } else {
            System.err.println("User " + username + " has not been accepted");
        }
    }

    /**
     * Disconnects the <code>disconnectedUser</code>.
     *
     * @param disconnectedUser the username of the player to be disconnected.
     */
    private void handleDisconnection(String disconnectedUser) {
        myController.handleDisconnection(disconnectedUser);
        synchronized (lockOnClientsNetworkStatus) {
            HeartBeat heartBeat = activeClients.get(disconnectedUser);
            heartBeat.terminate();
            activeClients.remove(disconnectedUser);
        }
        System.out.println("User " + disconnectedUser + " left the server :(");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(String username) throws RemoteException {
        handleDisconnection(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeStarter(String username, Side side) throws RemoteException {
        myController.placeStarter(username, side);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseColor(String username, PlayerColor color) throws RemoteException {
        myController.chooseColor(username, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeObjectiveCard(String username, int chosenObjective) throws RemoteException {
        myController.placeObjectiveCard(username, chosenObjective);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeCard(String username, int frontId, int backId, Side side, Position position) throws RemoteException {
        myController.placeCard(username, frontId, backId, side, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(String username, int idToDraw) throws RemoteException {
        myController.draw(username, idToDraw);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Message message) throws RemoteException {
        myController.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayersNumber(String username, int playersNumber) throws RemoteException {
        myController.setPlayersNumber(username, playersNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUnresponsiveness(String unresponsiveListener) throws RemoteException {
        System.err.println("Client " + unresponsiveListener + " has crashed");
        synchronized (lockOnClientsNetworkStatus) {
            handleDisconnection(unresponsiveListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        synchronized (lockOnClientsNetworkStatus) {
            HeartBeat heartBeat = activeClients.get(ping.getSender());
            //System.out.println("Received ping from " + ping.getSender());
            if (heartBeat==null) {
                System.err.println("which is unknown user: never connected or crashed");
            } else {
                //System.err.println("which is known");
                heartBeat.registerMessage(ping);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hear() throws RemoteException {

    }
}
