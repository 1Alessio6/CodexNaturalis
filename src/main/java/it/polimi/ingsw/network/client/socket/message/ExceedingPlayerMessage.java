package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class ExceedingPlayerMessage extends NetworkMessage {
    public ExceedingPlayerMessage() {
        super(Type.EXCEEDING_PLAYER, "server");
    }
}
