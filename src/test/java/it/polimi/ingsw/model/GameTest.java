package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Front;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GameTest {
    private List<String> usernames;
    private Game game;

    private List<String> createUsernames(int numUsername) {
        List<String> usernames = new ArrayList<>();
        for (int i = 0; i < numUsername; ++i) {
            usernames.add("user" + Integer.toString(i));
        }

        return usernames;
    }

    @Test
    void constructGame_noExceptions() {
        Assertions.assertDoesNotThrow(() -> {
            new Game(createUsernames(4));
        });
    }

    @Test
    void testInitialPhase() {
        game = new Game(createUsernames(4));
        Assertions.assertEquals(
                GamePhase.Setup,
                game.getPhase()
        );
    }

    @Test
    void finishSetup_phaseIsPlaceNormal() {
        usernames = createUsernames(2);
        game = new Game(createUsernames(usernames.size()));

        Assertions.assertDoesNotThrow(
                () -> {
                    // finish setup
                    for (int i = 0; i < usernames.size(); ++i) {
                        game.placeStarter(usernames.get(i), Side.FRONT);
                        game.assignColor(usernames.get(i), PlayerColor.BLUE);
                        game.placeObjectiveCard(usernames.get(i), 1);
                    }
                }
        );

        Assertions.assertEquals(GamePhase.PlaceNormal, game.getPhase());
    }

    @Test
    void testSkipTurn() {
        finishSetup_phaseIsPlaceNormal();
        game.skipTurn();
        Assertions.assertEquals(GamePhase.PlaceNormal, game.getPhase());
    }

}
