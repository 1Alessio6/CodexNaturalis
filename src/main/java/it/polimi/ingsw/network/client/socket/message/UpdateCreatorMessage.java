package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * UpdateCreatorMessage represents the message that executes the lobby after the arrival of the first player.
 */
public class UpdateCreatorMessage extends NetworkMessage {
    /**
     * Constructs an <code>UpdateCreatorMessage</code> message.
     */
    public UpdateCreatorMessage() {
        super(Type.UPDATE_CREATOR, "server");
    }
}
