package it.polimi.ingsw.network.client.socket.message;

public class UpdateAfterLobbyCrashMessage extends ClientMessage{

    public UpdateAfterLobbyCrashMessage() {
        super(ClientType.UPDATE_AFTER_LOBBY_CRASH);
    }
}
