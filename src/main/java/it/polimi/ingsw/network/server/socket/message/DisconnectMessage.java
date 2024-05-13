package it.polimi.ingsw.network.server.socket.message;

public class DisconnectMessage extends ServerMessage {
    public DisconnectMessage(String sender) {
        super(sender, ServerType.DISCONNECT);
    }
}
