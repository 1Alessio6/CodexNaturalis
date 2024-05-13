package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.chat.message.Message;

public class UpdateChatMessage extends ClientMessage{

    private final Message message;
    public UpdateChatMessage(Message message) {
        super(ClientType.SHOW_UPDATE_CHAT);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
