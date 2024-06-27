package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;
import it.polimi.ingsw.network.server.Server;

import java.rmi.RemoteException;

/**
 * ServerRMI is the remote reference for the client as if they called them locally.
 */
public class ServerRMI implements VirtualServer {
    private final static String SERVER_NAME = "ServerRmi";
    private final Server server;

    public static String getServerName() {
        return SERVER_NAME;
    }

    /**
     * Constructs the ServerRMI
     */
    public ServerRMI(Server server) {
        this.server = server;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(VirtualView client, String username) throws RemoteException {
        RMIHandler clientHandlerRMI = new RMIHandler(server, client, username);
        server.connect(clientHandlerRMI, username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(String username) throws RemoteException {
        server.disconnect(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeStarter(String username, Side side) throws RemoteException {
        server.placeStarter(username, side);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseColor(String username, PlayerColor color) throws RemoteException {
        server.chooseColor(username,color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeObjectiveCard(String username, int chosenObjective) throws RemoteException {
        server.placeObjectiveCard(username,chosenObjective);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeCard(String username, int frontId, int backId, Side side, Position position) throws RemoteException {
        server.placeCard(username, frontId, backId, side, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(String username, int idToDraw) throws RemoteException {
        server.draw(username, idToDraw);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Message message) throws RemoteException {
        server.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayersNumber(String username, int playersNumber) throws RemoteException {
        server.setPlayersNumber(username, playersNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        server.receivePing(ping);
    }

}
