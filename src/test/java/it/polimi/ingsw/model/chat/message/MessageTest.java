package it.polimi.ingsw.model.chat.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Test to check the correct passage of the parameters in order to instance a <code>Message</code> and the correct
 * creation of a broadcast and not broadcast message
 */
class MessageTest {
    /**
     * Test to check that passing a valid reference does not throw any kind of exception
     */
    @Test
    public void passValidReference_doesNotThrow() {
        Assertions.assertDoesNotThrow(() -> {
            new Message("a", "b", "c");
        });

        Assertions.assertDoesNotThrow(() -> {
            new Message("a", "c");
        });
    }

    /**
     * Test to check that a message created for a specific username isn't a message in broadcast
     */
    @Test
    public void createMessageForSpecificUsername_notBroadcast() {
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertFalse(new Message("a", "b", "c").isBroadcast());
        });
    }

    /**
     * Test to check that a message created for all users is a message in broadcast
     */
    @Test
    public void createMessageForAllUsers_isBroadcast() {
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertTrue(new Message("a", "c").isBroadcast());
        });
    }

    /**
     * Test to check that passing a null sender throws an <code>IllegalArgumentException</code>
     */
    @Test
    public void passNullSender_throwsException() {
        Assertions.assertThrows(InvalidMessageException.class, () -> {
            new Message(null, "b", "c");
        });
    }

    /**
     * Test to check that passing a null receiver throws an <code>IllegalArgumentException</code>
     */
    @Test
    public void passNullReceiver_throwsException() {
        Assertions.assertThrows(
                InvalidMessageException.class,
                () -> {
                    new Message("a", null, "c");
                });
    }

    /**
     * Test to check that passing a null content throws an <code>IllegalArgumentException</code>
     */
    @Test
    public void passNullContent_throwsException() {
        Assertions.assertThrows(
                InvalidMessageException.class,
                () -> {
                    new Message("a", "b", null);
                });
    }
}

