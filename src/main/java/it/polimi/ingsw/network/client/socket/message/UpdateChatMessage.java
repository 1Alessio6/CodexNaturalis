package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class UpdateChatMessage extends NetworkMessage {

    private final Message message;
    public UpdateChatMessage(Message message) {
        super(Type.SHOW_UPDATE_CHAT, "server");
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
