package it.polimi.ingsw.network.client.socket.message;

public class ClientPingMessage extends ClientMessage {
    private String sender;
    public ClientPingMessage(String sender) {
        super(ClientType.SEND_PING);
        this.sender = sender;
    }
    public String getUsername() {
        return sender;
    }
}
