package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.client.socket.message.ClientType;

public abstract class ClientMessage {
    ClientType type;

    public ClientMessage(ClientType type) {
        this.type = type;
    }

    ClientType getType() {
        return type;
    }
}
