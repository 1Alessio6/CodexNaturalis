package it.polimi.ingsw.network.server.socket.server;

public class PingMessage extends ServerMessage {
    public PingMessage(String sender) {
        super(sender, ServerType.SEND_PING);
    }
}
