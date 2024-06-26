package it.polimi.ingsw.network;

import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.model.lobby.LobbyListener;
import it.polimi.ingsw.network.heartbeat.HeartBeatListener;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;

/**
 * The ClientHandler interface defines the methods used to control the client representation on the server side.
 */
public interface ClientHandler extends GameListener, LobbyListener, HeartBeatListener {
    /**
     * Terminates the handler.
     */
    void terminate();

    /**
     * Registers the ping arrived to the server.
     * @param ping the ping sent by the client to the server.
     */
    void registerPingFromClient(HeartBeatMessage ping);

    /**
     * Starts the heartbeat to monitor the communication to the client.
     */
    void startHeartBeat();
}
