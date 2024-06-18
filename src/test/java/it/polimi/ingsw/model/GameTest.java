package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.PlainVirtualView;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test to check the correct functioning of the <code>Game</code> and its phases
 */
public class GameTest {
    private Game game;

    /**
     * Method used to create <code>numUsername</code> usernames
     *
     * @param numUsername the number of the usernames to create
     * @return a list of usernames
     */
    private List<String> createUsernames(int numUsername) {
        List<String> usernames = new ArrayList<>();
        for (int i = 0; i < numUsername; ++i) {
            usernames.add("user" + i);
        }

        return usernames;
    }

    /**
     * Test to check that the correct instance of a game doesn't throw any kind of exception
     */
    @Test
    void constructGame_noExceptions() {
        Assertions.assertDoesNotThrow(() -> {
            new Game(createUsernames(4));
        });
    }

    /**
     * Test to check if the initial phase coincides with the <code>Setup</code> game phase
     */
    @Test
    void testInitialPhase() {
        game = new Game(createUsernames(4));
        Assertions.assertEquals(
                GamePhase.Setup,
                game.getPhase()
        );
    }

    /**
     * Test to check if the phase after finishing the <code>Setup</code> is the <code>PlaceNormal</code>
     */
    @Test
    void finishSetup_phaseIsPlaceNormal() {
        List<String> usernames = createUsernames(2);
        game = new Game(usernames);
        List<PlayerColor> colors = new ArrayList<>(game.getAvailableColor());

        for (String user : usernames) {
            Assertions.assertDoesNotThrow(() -> game.add(user, new PlainVirtualView()));
        }

        Assertions.assertDoesNotThrow(
                () -> {
                    // finish setup
                    for (int i = 0; i < usernames.size(); ++i) {
                        game.placeStarter(usernames.get(i), Side.FRONT);
                        game.assignColor(usernames.get(i), colors.get(i));
                        game.placeObjectiveCard(usernames.get(i), 1);
                    }
                }
        );

        Assertions.assertEquals(GamePhase.PlaceNormal, game.getPhase());
    }

    /**
     * Test to check that a game phase is correctly skipped
     */
    @Test
    void testSkipTurn() {
        Assertions.assertDoesNotThrow(this::finishSetup_phaseIsPlaceNormal);
        String currentPlayerUsername = game.getCurrentPlayer().getUsername();
        game.skipTurn(currentPlayerUsername);
        Assertions.assertEquals(GamePhase.PlaceNormal, game.getPhase());
    }

}
