package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameTest {
    private Map<String, VirtualView> usernames;
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
    void finishSetup_phaseIsPlaceNormal() throws RemoteException {
        List<String> usernames = createUsernames(2);
        game = new Game(usernames);
        List<PlayerColor> colors = new ArrayList<>(game.getAvailableColor());

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

    @Test
    void testSkipTurn() {
        Assertions.assertDoesNotThrow(this::finishSetup_phaseIsPlaceNormal);
        String currentPlayerUsername = game.getCurrentPlayer().getUsername();
        game.skipTurn(currentPlayerUsername);
        Assertions.assertEquals(GamePhase.PlaceNormal, game.getPhase());
    }

}
