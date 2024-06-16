package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * SendChatMessage represents the message containing the message to be <B>sent</B> in chat.
 */
public class SendChatMessage extends NetworkMessage {
    Message message;

    /**
     * Constructs a <code>SendChatMessage</code> with the <code>sender</code> and <code>message</code> provided.
     *
     * @param sender  the message sender.
     * @param message the new message to be added to the chat.
     */
    public SendChatMessage(String sender, Message message) {
        super(Type.SEND_CHAT_MESSAGE, sender);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
