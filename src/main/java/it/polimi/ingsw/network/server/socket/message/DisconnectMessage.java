package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * DisconnectMessage represents the message that allows a player to disconnect from the game.
 */
public class DisconnectMessage extends NetworkMessage {

    /**
     * Constructs a <code>DisconnectMessage</code> with the <code>sender</code> provided.
     *
     * @param sender the username of the player to disconnect.
     */
    public DisconnectMessage(String sender) {
        super(Type.DISCONNECT, sender);
    }
}
