package it.polimi.ingsw.network;

import java.io.Serializable;

/**
 * Network Message represents the messages passed through the network.
 */
public class NetworkMessage implements Serializable {
    Type networkType;

    String sender;

    /**
     * Constructs a <code>NetworkMessage</code>.
     *
     * @param type   the network type.
     * @param sender the sender of the network message.
     */
    public NetworkMessage(Type type, String sender) {
        this.networkType = type;
        this.sender = sender;
    }

    public Type getNetworkType() {
        return networkType;
    }

    public String getSender() {
        return sender;
    }
}
