package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.chat.message.Message;
import org.junit.jupiter.api.*;

import java.util.List;

class ChatDatabaseTest {
    private ChatDatabase chatDataBase;


    @BeforeEach
    public void setUp() {
        chatDataBase = new ChatDatabase();
    }

    @Test
    public void addNullMessage_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {
                    chatDataBase.addMessage(null);
        });
    }

    @Test
    public void addValidMessage_doesNotThrow() {
        Assertions.assertDoesNotThrow(() ->  {
            chatDataBase.addMessage(new Message("a", "b", "content"));
        });
    }

    @Test
    public void addValidMessage_addsToMessageList() {
        String senderUsername = "a";
        String receiverUsername = "b";
        String content = "c";
        chatDataBase.addMessage(new Message(senderUsername, receiverUsername, content));

        List<Message> messages = chatDataBase.getMessages();

        // the two lists must contain only one element and must be equal
        Assertions.assertEquals(1, messages.size());

        Message m = messages.get(0);
        // the message contained must be the right one
        Assertions.assertEquals(senderUsername, m.getSender());
        Assertions.assertEquals(receiverUsername, m.getRecipient());
        Assertions.assertEquals(content, m.getContent());
        Assertions.assertFalse(m.isBroadcast());
    }

}