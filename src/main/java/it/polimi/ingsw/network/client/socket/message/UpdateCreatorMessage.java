package it.polimi.ingsw.network.client.socket.message;

public class UpdateCreatorMessage extends ClientMessage{
    public UpdateCreatorMessage() {
        super(ClientType.UPDATE_CREATOR);
    }
}
