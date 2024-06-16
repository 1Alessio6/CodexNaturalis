package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * ConnectMessage represents the message that allows a player to connect to the game.
 */
public class ConnectMessage extends NetworkMessage {

    /**
     * Constructs a <code>ConnectMessage</code> with the <code>sender</code> provided.
     *
     * @param sender the username of the player to connect.
     */
    public ConnectMessage(String sender) {
        super(Type.CONNECT, sender);
    }
}
