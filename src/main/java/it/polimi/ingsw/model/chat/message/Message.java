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

    private boolean isBroadcast;

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

    public static String getNameForGroup() {
        return NAME_FOR_GROUP;
    }

    private static void checkMessageInfo(String sender, String receiver, String content) throws InvalidMessageException {
        if (sender == null) {
            throw new InvalidMessageException("sender" + " has not been specified");
        }

        if (receiver == null) {
            throw new InvalidMessageException("receiver"+ " has not been specified");
        }

        if (content == null || content.isEmpty()) {
            throw new InvalidMessageException("message cannot be empty");
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

    public Message(String sender, String receiver, String content) throws InvalidMessageException {
        checkMessageInfo(sender, receiver, content);
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.isBroadcast = false;
    }

    public void setBroadcast() {
        isBroadcast = true;
    }

    /**
     * Constructs a message from sender to all players.
     *
     * @param sender  sender's username.
     * @param content message content.
     * @throws IllegalArgumentException if any argument is null.
     */
    public Message(String sender, String content) throws InvalidMessageException {
        checkMessageInfo(sender, "", content);
        this.sender = sender;
        this.receiver = NAME_FOR_GROUP;
        this.content = content;
        this.isBroadcast = true;
    }

    @Override
    public String toString() {
        return
                sender + " to " +
                receiver + ": " +
                content;
    }
}
