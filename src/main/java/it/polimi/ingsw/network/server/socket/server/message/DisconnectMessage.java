package it.polimi.ingsw.network.server.socket.server.message;

public class DisconnectMessage extends ServerMessage {
    public DisconnectMessage(String sender) {
        super(sender, ServerType.DISCONNECT);
    }
}
