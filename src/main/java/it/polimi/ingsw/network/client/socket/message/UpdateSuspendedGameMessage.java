package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class UpdateSuspendedGameMessage extends NetworkMessage {
    public UpdateSuspendedGameMessage() {
        super(Type.SHOW_UPDATE_SUSPENDED_GAME, "server");
    }
}
