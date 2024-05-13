package it.polimi.ingsw.network.server.socket.server.message;

public class ConnectMessage extends ServerMessage {
    public ConnectMessage(String sender) {
        super(sender, ServerType.CONNECT);
    }
}
