package it.polimi.ingsw.model.chat.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class MessageTest {
    @Test
    public void passValidReference_doesNotThrow() {
        Assertions.assertDoesNotThrow(() -> {
            new Message("a", "b", "c");
        });

        Assertions.assertDoesNotThrow(() -> {
            new Message("a", "c");
        });
    }

    @Test
    public void createMessageForSpecificUsername_notBroadcast() {
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertFalse(new Message("a", "b", "c").isBroadcast());
        });
    }

    @Test
    public void createMessageForAllUsers_isBroadcast() {
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertTrue(new Message("a", "c").isBroadcast());
        });
    }

    @Test
    public void passNullSender_throwsException() {
        Assertions.assertThrows(InvalidMessageException.class, () -> {
            new Message(null, "b", "c");
        });
    }

    @Test
    public void passNullReceiver_throwsException() {
        Assertions.assertThrows(
                InvalidMessageException.class,
                () -> {
                    new Message("a", null, "c");
                });
    }

    @Test
    public void passNullContent_throwsException() {
        Assertions.assertThrows(
                InvalidMessageException.class,
                () -> {
                    new Message("a", "b", null);
                });
    }
}

