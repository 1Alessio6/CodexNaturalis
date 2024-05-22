package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class ConnectMessage extends NetworkMessage {
    public ConnectMessage(String sender) {
        super(Type.CONNECT, sender);
    }
}
