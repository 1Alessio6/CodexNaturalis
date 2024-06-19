package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class FullLobbyMessage extends NetworkMessage {
    public FullLobbyMessage() {
        super(Type.FULL_LOBBY, "server");
    }
}
