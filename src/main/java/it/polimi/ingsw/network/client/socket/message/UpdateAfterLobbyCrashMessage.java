package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class UpdateAfterLobbyCrashMessage extends NetworkMessage {

    public UpdateAfterLobbyCrashMessage() {
        super(Type.UPDATE_AFTER_LOBBY_CRASH, "server");
    }
}
