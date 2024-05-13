package it.polimi.ingsw.network.server.socket.server;

public class ConnectMessage extends ServerMessage {
    public ConnectMessage(String sender) {
        super(sender, ServerType.CONNECT);
    }
}
