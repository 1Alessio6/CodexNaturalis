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

    public Chat(String author) {
        this.author = author;
    }

    public void selectRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void insertText(String content) {
        this.content = content;
    }

    public void sendMessage(ClientController controller) throws RemoteException, InvalidMessageException {
        Message toSend = new Message(author, recipient, content);
        if (recipient.equals("Everyone")) {
            toSend.setBroadcast();
        }
        controller.sendMessage(toSend);
        content = null;
    }
}
