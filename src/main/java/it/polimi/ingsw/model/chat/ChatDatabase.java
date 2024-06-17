package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.chat.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat database to store all messages sent by players.
 */
public class ChatDatabase {
    /**
     * FIFO to store messages sent by players in order of their arrival.
     */
    private final List<Message> messageList;

    /**
     * Constructs a ChatDatabase with no parameters provided.
     */
    public ChatDatabase() {
        messageList = new ArrayList<>();
    }

    /**
     * Stores the message.
     *
     * @param message to be stored.
     * @throws IllegalArgumentException if message is null.
     */
    public void addMessage(Message message) throws IllegalArgumentException {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        messageList.add(message);
    }

    /**
     * Returns the messages sent by the players in order of arrival time: the oldest is the leftmost one.
     *
     * @return messages sent by the players.
     */
    public List<Message> getMessages() {
        return messageList;
    }
}
