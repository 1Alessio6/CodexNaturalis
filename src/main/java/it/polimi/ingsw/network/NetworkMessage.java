package it.polimi.ingsw.network;

import java.io.Serializable;

public class NetworkMessage implements Serializable {
    Type networkType;

    String sender;

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
