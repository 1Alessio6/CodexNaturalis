package it.polimi.ingsw.network.server.socket.server;

import it.polimi.ingsw.model.chat.message.Message;

public class SendChatMessage extends ServerMessage {
    Message message;
    public SendChatMessage(String sender, Message message) {
        super(sender, ServerType.SEND_CHAT_MESSAGE);
        this.message = message;
    }
}
