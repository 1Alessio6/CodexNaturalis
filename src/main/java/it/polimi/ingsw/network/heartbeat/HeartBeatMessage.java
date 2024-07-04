package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

import java.io.Serializable;

/**
 * HeartBeatMessage represents the message containing the sender and the id number of the message.
 */
public class HeartBeatMessage extends NetworkMessage implements Serializable{
    private final double id;
    /**
     * Constructs a <code>HeartBeatMessage</code> with the <code>sender</code> and the <code>id</code> provided.
     *
     * @param sender the sender of the heartBeat.
     * @param id     the identification of the heartBeat message.
     */
    public HeartBeatMessage(String sender, double id) {
        super(Type.HEARTBEAT, sender);
        this.id = id;
    }

    public double getId() {
        return id;
    }
}
