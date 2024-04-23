package it.polimi.ingsw.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GameTest {
    private List<String> usernames;

    private List<String> createUsernames(int numUsername) {
        List<String> usernames = new ArrayList<>();
        for (int i = 0; i < numUsername; ++i) {
            usernames.add(Character.toString((char) ('a' + i)));
        }

        return usernames;
    }

    @Test
    void constructGame_noExceptions() {
        Assertions.assertDoesNotThrow(() -> {
            new Game(createUsernames(4));
        });
    }

}
