package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.VirtualView;

public class ConnectMessage extends ServerMessage {

    private final VirtualView client;
    public ConnectMessage(VirtualView client, String sender) {
        super(sender, ServerType.CONNECT);
        this.client = client;
    }

    public VirtualView getClient() {
        return client;
    }
}
