package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * UpdateSuspendedGameMessage represents the message indicating the suspension of the game.
 */
public class UpdateSuspendedGameMessage extends NetworkMessage {
    /**
     * Constructs an <code>UpdateSuspendedGameMessage</code>.
     */
    public UpdateSuspendedGameMessage() {
        super(Type.SHOW_UPDATE_SUSPENDED_GAME, "server");
    }
}
