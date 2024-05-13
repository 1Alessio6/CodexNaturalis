package it.polimi.ingsw.network.server.socket.client.clientmessage;

public abstract class ClientMessage {
    ClientType type;

    public ClientMessage(ClientType type) {
        this.type = type;
    }

    ClientType getType() {
        return type;
    }
}
