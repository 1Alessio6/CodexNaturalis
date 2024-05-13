package it.polimi.ingsw.network.server.socket.message;

public class ServerMessage {
    ServerType type;
    String sender;

        public ServerMessage(String sender, ServerType type) {
        this.type = type;
        this.sender = sender;
    }

    public ServerType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }
}
