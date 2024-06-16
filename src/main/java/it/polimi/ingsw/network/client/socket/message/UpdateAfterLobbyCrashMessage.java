package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * UpdateAfterLobbyCrashMessage represents the message indicating the crash of the lobby.
 */
public class UpdateAfterLobbyCrashMessage extends NetworkMessage {

    /**
     * Constructs an <code>UpdateAfterLobbyCrashMessage</code>.
     */
    public UpdateAfterLobbyCrashMessage() {
        super(Type.UPDATE_AFTER_LOBBY_CRASH, "server");
    }
}
