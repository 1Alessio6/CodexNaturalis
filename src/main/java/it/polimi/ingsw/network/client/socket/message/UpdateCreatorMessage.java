package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class UpdateCreatorMessage extends NetworkMessage {
    public UpdateCreatorMessage() {
        super(Type.UPDATE_CREATOR, "server");
    }
}
