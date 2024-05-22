package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class DisconnectMessage extends NetworkMessage {
    public DisconnectMessage(String sender) {
        super(Type.DISCONNECT, sender);
    }
}
