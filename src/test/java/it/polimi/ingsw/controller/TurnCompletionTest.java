package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.action.PlayerAction;
import it.polimi.ingsw.model.player.action.PlayerState;
import it.polimi.ingsw.network.client.ClientRMI;
import it.polimi.ingsw.network.client.model.ClientGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurnCompletionTest {
    private Game game;
    private TurnCompletion turnCompletion;

    @BeforeEach
    void setUp() {
        game = new Game(Arrays.asList("user1", "user2", "user3", "user4"));
        turnCompletion = new TurnCompletion();
    }

    @Test
    void disconnectsPlayersInSetup_CompleteSetup() {
        List<String> users  = game.getPlayers().stream().map(Player::getUsername).toList();
        for (String user : users) {
            Assertions.assertDoesNotThrow(() -> {
                game.remove(user);
            });
            turnCompletion.handleLeave(game);
        }

        Assertions.assertFalse(game.isActive());
        for (int i = 0; i < users.size() - 1; ++i) {
            PlayerState playerState = game.getPlayerByUsername(users.get(i)).getPlayerAction().getPlayerState();
            Assertions.assertEquals(playerState, PlayerState.Place);
        }

        // the last player setup is not completed because the game has been suspended
        PlayerState lastPlayerState = game.getPlayerByUsername(users.getLast()).getPlayerAction().getPlayerState();
        Assertions.assertEquals(lastPlayerState, PlayerState.ChooseStarter);

        Assertions.assertDoesNotThrow(() -> {
            game.add(users.getFirst(), new ClientRMI(new ClientGame(game)));
            turnCompletion.handleJoin(game);
        });
        Assertions.assertFalse(game.isActive());

        // add another player to make the game active again
        Assertions.assertDoesNotThrow(() -> {
            game.add(users.get(1), new ClientRMI(new ClientGame(game)));
            turnCompletion.handleJoin(game);
        });
        Assertions.assertTrue(game.isActive());

        // two players are active, the game is not suspended anymore, thus the setup of the last player should be completed
        Assertions.assertTrue(game.isActive());
        PlayerState playerState = game.getPlayerByUsername(users.getLast()).getPlayerAction().getPlayerState();
        Assertions.assertEquals(playerState, PlayerState.Place);
        Assertions.assertEquals(GamePhase.PlaceNormal, game.getPhase());
    }
}