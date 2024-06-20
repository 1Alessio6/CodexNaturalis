package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * ExceedingPlayerMessage represents the message indicating the player who has exceeded the limit set by the first player
 * that he has exceeded the limit.
 */
public class ExceedingPlayerMessage extends NetworkMessage {
    /**
     * Constructs an <code>ExceedingPlayerMessage</code>.
     */
    public ExceedingPlayerMessage() {
        super(Type.EXCEEDING_PLAYER, "server");
    }
}
