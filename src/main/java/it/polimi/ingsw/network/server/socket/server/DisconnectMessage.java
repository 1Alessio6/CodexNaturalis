package it.polimi.ingsw.network.server.socket.server;

public class DisconnectMessage extends ServerMessage {
    public DisconnectMessage(String sender) {
        super(sender, ServerType.DISCONNECT);
    }
}
