package it.polimi.ingsw.network.client.socket.message;

public class UpdateSuspendedGameMessage extends ClientMessage {
    public UpdateSuspendedGameMessage() {
        super(ClientType.SHOW_UPDATE_SUSPENDED_GAME);
    }
}
