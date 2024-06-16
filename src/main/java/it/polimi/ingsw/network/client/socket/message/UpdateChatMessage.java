package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * UpdateChatMessage represents the message containing the message to <B>update</B> the chat.
 */
public class UpdateChatMessage extends NetworkMessage {

    private final Message message;

    /**
     * Constructs an <code>UpdateChatMessage</code> with the <code>message</code>.
     * The sender is the server by default.
     *
     * @param message the new message to be displayed in chat.
     */
    public UpdateChatMessage(Message message) {
        super(Type.SHOW_UPDATE_CHAT, "server");
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
