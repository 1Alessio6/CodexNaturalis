package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;
import it.polimi.ingsw.network.heartbeat.HeartBeatListener;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface VirtualServer extends Remote, HeartBeatHandler, HeartBeatListener {
    /**
     * Connect to server
     *
     * @param client
     * @throws RemoteException
     */
    void connect(VirtualView client, String username) throws RemoteException;

    void placeStarter(String username, Side side) throws RemoteException;

    void chooseColor(String username, PlayerColor color) throws RemoteException;

    void placeObjectiveCard(String username, int chosenObjective) throws RemoteException;

    void placeCard(String username, int frontId, int backId, Side side, Position position) throws RemoteException;

    void draw(String username, int idToDraw) throws RemoteException;

    void sendMessage(Message message) throws RemoteException;

    void setPlayersNumber(String username, int playersNumber) throws RemoteException;

    void disconnect(String username) throws RemoteException;

    void hear() throws RemoteException;
}
