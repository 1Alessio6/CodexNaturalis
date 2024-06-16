package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * UpdatePlayerStatusMessage represents the message containing the status of the player.
 */
public class UpdatePlayerStatusMessage extends NetworkMessage {

    private final boolean isConnected;
    private final String username;

    /**
     * Constructs an <code>UpdatePlayerStatusMessage</code> with the status and <code>username</code> provided.
     *
     * @param isConnected the player status.
     * @param username    the username of the player.
     */
    public UpdatePlayerStatusMessage(boolean isConnected, String username) {
        super(Type.SHOW_UPDATE_PLAYER_STATUS, "server");
        this.isConnected = isConnected;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
