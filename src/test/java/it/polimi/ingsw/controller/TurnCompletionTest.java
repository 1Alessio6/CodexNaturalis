package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.action.PlayerState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test to check the correct turn completion
 */
class TurnCompletionTest {
    private Game game;
    private TurnCompletion turnCompletion;

    /**
     * Creates a new turnCompletion and game instance along with a list of users before each test
     */
    @BeforeEach
    void setUp() {
        List<String> users = Arrays.asList("user1", "user2", "user3", "user4");
        game = new Game(users);
        for (String user: users){
            Assertions.assertDoesNotThrow(() -> {
                game.add(user, new PlainGameListener());
            });
        }
        turnCompletion = new TurnCompletion();
    }

    /**
     * Tests turnCompletion in set up phase, distinguishing between the case in which the game is suspended and the case
     * in which the game isn't suspended
     */
    @Test
    void disconnectsPlayersInSetup_CompleteSetup() {
        List<String> users = game.getPlayers().stream().map(Player::getUsername).toList();
        for (String user : users) {
            Assertions.assertDoesNotThrow(() -> {
                game.remove(user);
            });
            turnCompletion.handleLeave(game);
        }

        Assertions.assertFalse(game.isActive());
        for (int i = 0; i < users.size(); ++i) {
            PlayerState playerState = game.getPlayerByUsername(users.get(i)).getPlayerAction().getPlayerState();
            Assertions.assertEquals(playerState, PlayerState.Place);
        }

        // check the current player is not changed
        Assertions.assertEquals(0, game.getCurrentPlayerIdx());

        //PlayerState lastPlayerState = game.getPlayerByUsername(users.getLast()).getPlayerAction().getPlayerState();
        //Assertions.assertEquals(lastPlayerState, PlayerState.Place);

        Assertions.assertDoesNotThrow(() -> {
            game.add(users.getFirst(), new PlainGameListener());
            turnCompletion.handleJoin(game);
        });
        Assertions.assertFalse(game.isActive());

        // add another player to make the game active again
        Assertions.assertDoesNotThrow(() -> {
            game.add(users.get(1), new PlainGameListener());
            turnCompletion.handleJoin(game);
        });
        Assertions.assertTrue(game.isActive());

        // the current player has to be chosen
        Assertions.assertTrue(game.getCurrentPlayer().isConnected());
    }

    /**
     * Method used to complete the set-up phase
     *
     * @param game the representation of the game
     * @param user the username of the player
     */
    private void completeSetup(Game game, String user) {
        Assertions.assertDoesNotThrow(() -> {
            game.placeStarter(user, Side.FRONT);
            game.assignColor(user, new ArrayList<>(game.getAvailableColor()).getFirst());
            game.placeObjectiveCard(user, 0);
        });
    }

    /**
     * Method used to place a card
     *
     * @param game          the representation of the game
     * @param currentPlayer the username of the player who performs the placement
     */
    private void place(Game game, Player currentPlayer) {
        Assertions.assertDoesNotThrow(() -> {
            game.placeCard(currentPlayer.getUsername(), currentPlayer.getCards().getFirst(), Side.FRONT, currentPlayer.getAvailablePositions().getFirst());
        });
    }

    /**
     * Corrects the evolution of the current player
     *
     * @param oldCurrentPlayerName the username of the old current player
     */
    void correctEvolutionOfTheCurrentPlayer(String oldCurrentPlayerName) {
        List<String> users = game.getPlayers().stream().map(Player::getUsername).toList();
        // correct evolution of the current player
        boolean oneActivePlayer = false;
        String currPlayerName = "";
        for (String user: users) {
            if(game.getCurrentPlayer().getUsername().equals(user)) {
                oneActivePlayer = true;
                currPlayerName = user;
                break;
            }
        }
        Assertions.assertTrue(oneActivePlayer);
        Assertions.assertNotEquals(currPlayerName, oldCurrentPlayerName);
    }

    /**
     * Tests if the disconnection of the current player immediately after the completion of the set-up leads to the
     * correctly completed place player action
     */
    @Test
    void disconnectsCurrentPlayer_completePlace() {
        List<String> users = game.getPlayers().stream().map(Player::getUsername).toList();
        for (String user : users) {
            completeSetup(game, user);
        }

        String oldCurrentPlayerName = game.getCurrentPlayer().getUsername();
        Assertions.assertDoesNotThrow(() -> game.remove(oldCurrentPlayerName));
        turnCompletion.handleLeave(game);
        correctEvolutionOfTheCurrentPlayer(oldCurrentPlayerName);
        Assertions.assertNotEquals(oldCurrentPlayerName, game.getCurrentPlayer().getUsername());
        Assertions.assertEquals(game.getPlayerByUsername(oldCurrentPlayerName).getPlayerAction().getPlayerState(), PlayerState.Place);
    }

    /**
     * Tests if the disconnection of the current player immediately after a place leads to the correctly completed draw
     * player action
     */
    @Test
    void disconnectsCurrentPlayer_completeDraw() {
        List<String> users = game.getPlayers().stream().map(Player::getUsername).toList();
        for (String user : users) {
            completeSetup(game, user);
        }
        Player oldCurrentPlayer = game.getCurrentPlayer();
        place(game, oldCurrentPlayer);
        Assertions.assertDoesNotThrow(() -> game.remove(oldCurrentPlayer.getUsername()));
        List<Card> inHandBefore = new ArrayList<>(oldCurrentPlayer.getCards());
        turnCompletion.handleLeave(game);
        correctEvolutionOfTheCurrentPlayer(oldCurrentPlayer.getUsername());
        List<Card> inHandAfter = new ArrayList<>(oldCurrentPlayer.getCards());
        Assertions.assertEquals(oldCurrentPlayer.getPlayerAction().getPlayerState(), PlayerState.Place);
        Assertions.assertFalse(inHandBefore.equals(inHandAfter));
    }

    /**
     * Tests if the disconnection of the current player when the game is suspended does not lead to the completion of
     * any action
     */
    @Test
    void disconnectsCurrentPlayerInSuspendedGame_doNothing() {
        List<String> users = game.getPlayers().stream().map(Player::getUsername).toList();
        for (String user : users) {
            completeSetup(game, user);
        }
        Player currentPlayer = game.getCurrentPlayer();
        for (String user : users) {
            if (!user.equals(currentPlayer.getUsername())) {
                Assertions.assertDoesNotThrow(() -> {
                    game.remove(user);
                });
                turnCompletion.handleLeave(game);
            }
        }

        Assertions.assertDoesNotThrow(() -> {
            game.remove(currentPlayer.getUsername());
        });
        List<Card> inHandBefore = currentPlayer.getCards();
        turnCompletion.handleLeave(game);
        List<Card> inHandAfter = currentPlayer.getCards();
        Assertions.assertEquals(inHandBefore, inHandAfter);
    }

    /**
     * Method used to complete the set-up phase for all players in the game
     *
     * @param game the representation of the game
     */
    private void completeSetup(Game game) {
        for (Player player : game.getPlayers()) {
            Assertions.assertDoesNotThrow(() -> {
                String username = player.getUsername();
                game.placeStarter(username, Side.FRONT);
                game.assignColor(username, new ArrayList<>(game.getAvailableColor()).getFirst());
                game.placeObjectiveCard(username, 0);
            });
        }
    }

    /**
     * Tests if after the disconnection of the current and the next players, the current player is updated correctly
     */
    @Test
    void disconnectCurrenPlayerAndNextPlayers_currentPlayerIsUpdatedCorrectly() {
        int numPlayers = 4;

        completeSetup(game);

        List<Player> players = game.getPlayers();
        for (int i = 0; i < numPlayers; ++i) {
            for (int lastConnectedPlayerIdx = i + 1; lastConnectedPlayerIdx < numPlayers - 2; ++lastConnectedPlayerIdx) {
                for (int disconnectedPlayerIdx = i; disconnectedPlayerIdx < lastConnectedPlayerIdx; ++disconnectedPlayerIdx) {
                    int disconnectedPlayerIdxCopy = disconnectedPlayerIdx;
                    Assertions.assertDoesNotThrow(() -> {
                        game.remove(players.get(disconnectedPlayerIdxCopy).getUsername());
                        turnCompletion.handleLeave(game);
                    });
                }
                Assertions.assertEquals(game.getCurrentPlayer().getUsername(), players.get(lastConnectedPlayerIdx).getUsername());
            }
        }
    }
}