package it.polimi.ingsw.network.client.view.gui.util.chat;

import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.client.controller.ClientController;

import java.rmi.RemoteException;

/**
 * Registers messages that will be sent to the server.
 */
public class Chat {
    private final String author;
    private String recipient;
    private String content;

    /**
     * Constructs a <code>Chat</code>
     *
     * @param author the author of the message
     */
    public Chat(String author) {
        this.author = author;
    }

    /**
     * Selects the recipient of the message
     *
     * @param recipient the message recipient
     */
    public void selectRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Inserts the <code>content</code> of the message
     *
     * @param content the message content
     */
    public void insertText(String content) {
        this.content = content;
    }

    /**
     * Sends a message
     *
     * @param controller the representation of the controller
     * @throws InvalidMessageException if the author doesn't match the author or the recipient doesn't exist
     */
    public void sendMessage(ClientController controller) throws InvalidMessageException {
        Message toSend = new Message(author, recipient, content);
        if (recipient.equals("Everyone")) {
            toSend.setBroadcast();
        }
        controller.sendMessage(toSend);
        content = null;
    }
}
