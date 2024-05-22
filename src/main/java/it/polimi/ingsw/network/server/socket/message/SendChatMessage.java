package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class SendChatMessage extends NetworkMessage {
    Message message;

    public SendChatMessage(String sender, Message message) {
        super(Type.SEND_CHAT_MESSAGE, sender);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
