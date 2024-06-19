package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.chat.message.Message;
import org.junit.jupiter.api.*;

import java.util.List;

/**
 * Test to check the correct passage of the parameters in order to add a message in the <code>ChatDatabase</code>
 */
class ChatDatabaseTest {
    private ChatDatabase chatDataBase;

    /**
     * Creates a new <code>ChatDatabase</code> before each test
     */
    @BeforeEach
    public void setUp() {
        chatDataBase = new ChatDatabase();
    }

    /**
     * Test to check that adding a null message throws an <code>IllegalArgumentException</code>
     */
    @Test
    public void addNullMessage_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {
                    chatDataBase.addMessage(null);
        });
    }

    /**
     * Test to check that adding a valid message doesn't throw any kind of exception
     */
    @Test
    public void addValidMessage_doesNotThrow() {
        Assertions.assertDoesNotThrow(() ->  {
            chatDataBase.addMessage(new Message("a", "b", "content"));
        });
    }

    /**
     * Test to check that by adding a valid message it is correctly added to the <code>ChatDatabase</code>
     */
    @Test
    public void addValidMessage_addsToMessageList() {
        String senderUsername = "a";
        String receiverUsername = "b";
        String content = "c";
        Assertions.assertDoesNotThrow(() -> {
            chatDataBase.addMessage(new Message(senderUsername, receiverUsername, content));
        });
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