package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * FullLobbyMessage represents the message indicating the fullness of the lobby.
 */
public class FullLobbyMessage extends NetworkMessage {
    /**
     * Constructs a <code>FullLobbyMessage</code>.
     */
    public FullLobbyMessage() {
        super(Type.FULL_LOBBY, "server");
    }
}
