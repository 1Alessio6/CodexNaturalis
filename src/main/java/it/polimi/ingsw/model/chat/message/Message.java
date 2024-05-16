package it.polimi.ingsw.model.chat.message;

import java.io.Serializable;

/**
 * Message sent by players.
 */
public class Message implements Serializable {
    private static final String NAME_FOR_GROUP = "Everyone"; // Message from X to every user will be displayed as "X to NAME_FOR_GROUP"
    private final String sender;
    private final String receiver;
    private final String content;

    private final boolean isBroadcast;

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    private static void checkMessageInfo(String sender, String receiver, String content) throws IllegalArgumentException {
        if (sender == null) {
            throw new IllegalArgumentException("sender" + " must be a valid reference");
        }

        if (receiver == null) {
            throw new IllegalArgumentException("receiver"+ " must be a valid reference");
        }

        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("invalid content");
        }
    }

    /**
     * Constructs a message from sender to receiver.
     *
     * @param sender   sender's username.
     * @param receiver receiver's username.
     * @param content  message content.
     * @throws IllegalArgumentException if any argument is null.
     */

    public Message(String sender, String receiver, String content) throws IllegalArgumentException {
        checkMessageInfo(sender, receiver, content);
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.isBroadcast = false;
    }

    /**
     * Constructs a message from sender to all players.
     *
     * @param sender  sender's username.
     * @param content message content.
     * @throws IllegalArgumentException if any argument is null.
     */
    public Message(String sender, String content) throws IllegalArgumentException {
        checkMessageInfo(sender, "", content);
        this.sender = sender;
        this.receiver = NAME_FOR_GROUP;
        this.content = content;
        this.isBroadcast = true;
    }

}
