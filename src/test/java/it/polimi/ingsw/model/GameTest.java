package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.PlainGameListener;
import it.polimi.ingsw.controller.TurnCompletion;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.gamephase.GamePhase;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to check the correct functioning of the <code>Game</code> and its phases
 */
public class GameTest {
    private Game game;
    private List<String> users;
    private TurnCompletion turnCompletion;

    /**
     * Method used to create <code>numUsername</code> usernames
     *
     * @return a list of usernames
     */
    private List<String> createUsernames() {
        List<String> usernames = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            usernames.add("user" + i);
        }

        return usernames;
    }

    private void addViews() throws InvalidUsernameException {
        for (String user : users) {
            game.add(user, new PlainGameListener());
        }
    }

    /**
     * Test to check that the correct instance of a game doesn't throw any kind of exception
     */
    @BeforeEach
    void setup() {
        users = createUsernames();
        Assertions.assertDoesNotThrow(() -> {
            game = new Game(users);

            // add view for connection simulation
            addViews();
        });
        turnCompletion = new TurnCompletion();
    }

    /**
     * Test to check if the initial phase coincides with the <code>Setup</code> game phase
     */
    @Test
    void testInitialPhase() {
        Assertions.assertEquals(
                GamePhase.Setup,
                game.getPhase()
        );
    }

    @Test
    void removeAll() {
        String notPresent = "notPresent";

        assertThrows(InvalidUsernameException.class, () -> game.remove(notPresent));

        // game is full
        for (String username : users) {
            assertDoesNotThrow(() -> game.remove(username));
        }

        assertFalse(game.isActive() && !game.getActivePlayers().isEmpty());
    }

    @Test
    void add() {
        // game will have players, as it's initialized
        String outsider = "notPresent";

        List<String> connectedPlayers = game.getActivePlayers().stream().map(Player::getUsername).toList();
        List<String> disconnectedPlayers = new ArrayList<>(users);
        disconnectedPlayers.removeAll(connectedPlayers);

        assertThrows(InvalidUsernameException.class, () -> game.add(outsider, new PlainGameListener()));

        for (String online : connectedPlayers) {
            assertThrows(InvalidUsernameException.class, () -> game.add(online, new PlainGameListener()));
        }

        for (String afk : disconnectedPlayers) {
            assertDoesNotThrow(() -> game.add(afk, new PlainGameListener()));
        }
    }

    /**
     * Test to check if the phase after finishing the <code>Setup</code> is the <code>PlaceNormal</code>
     */
    @Test
    void finishSetup_phaseIsPlaceNormal() {
        // remove one player a time, to not make game go inactive
        for (String username : users) {
            assertDoesNotThrow(() -> game.remove(username));
            turnCompletion.handleLeave(game);
            assertDoesNotThrow(() -> game.add(username, new PlainGameListener()));
        }

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

    @Test
    void placeCard() {
        // go directly to placeNormal Phase
        finishSetup_phaseIsPlaceNormal();

        // test place when game is inactive
        Player currentPlayer = game.getCurrentPlayer();
        for (String username : users) {
            if (!currentPlayer.getUsername().equals(username)) {
                assertDoesNotThrow(() -> game.remove(username));
            }
        }

        assertThrows(SuspendedGameException.class, () -> game.placeCard(currentPlayer.getUsername(),
                currentPlayer.getCards().getFirst(),
                Side.BACK, new Position(1,1)));

        add();

        for (Player player : game.getPlayers()) {
            if (!player.equals(currentPlayer))
                assertThrows(InvalidPlayerActionException.class,
                        () -> game.placeCard(player.getUsername(), player.getCards().getFirst(),
                                Side.BACK, new Position(1,1)));
            else {
                // search card that will require resource: at the beginning there won't be enough
                Card golden = player.getCards().stream()
                        .filter(c -> !c.getRequiredResources().isEmpty()).findAny().get();
                assertThrows(Playground.NotEnoughResourcesException.class,
                        () -> game.placeCard(player.getUsername(), golden,
                                Side.FRONT, new Position(1,1)));

                // back won't have problem, but position is wrong
                assertThrows(Playground.UnavailablePositionException.class,
                        () -> game.placeCard(player.getUsername(), golden,
                                Side.BACK, new Position(10,10)));

                assertDoesNotThrow(() -> game.placeCard(player.getUsername(), player.getCards().getFirst(),
                        Side.BACK, new Position(1,1)));

                // can place only one card a time
                assertThrows(InvalidGamePhaseException.class,
                        () -> game.placeCard(player.getUsername(), player.getCards().getLast(),
                                Side.BACK, new Position(-1,-1)));
            }
        }
    }

    @Test
    void drawFromFaceUpCards() {
        // resume after place
        placeCard();

        Player currentPlayer = game.getCurrentPlayer();
        List<Player> notCurrent = new ArrayList<>(game.getPlayers());
        notCurrent.remove(currentPlayer);

        // before action invalid
        for (Player player : notCurrent) {
            assertThrows(InvalidPlayerActionException.class,
                    () -> game.drawFromFaceUpCards(player.getUsername(), 1));
        }

        // test current
        assertThrows(AssertionError.class,
                () -> game.drawFromFaceUpCards(currentPlayer.getUsername(), 5));// 5 or 4 are for decks

        assertDoesNotThrow(() -> game.drawFromFaceUpCards(currentPlayer.getUsername(), 1));

        // can draw only one card a time
        assertThrows(InvalidGamePhaseException.class,
                () -> game.drawFromFaceUpCards(currentPlayer.getUsername(), 1));

        // after is invalid phase
        for (Player player : notCurrent) {
            assertThrows(InvalidGamePhaseException.class,
                    () -> game.drawFromFaceUpCards(player.getUsername(), 1));
        }
    }

    @Test
    void drawFromDeck() {
        // resume after place
        placeCard();

        Player currentPlayer = game.getCurrentPlayer();
        List<Player> notCurrent = new ArrayList<>(game.getPlayers());
        notCurrent.remove(currentPlayer);

        // before action invalid
        for (Player player : notCurrent) {
            assertThrows(InvalidPlayerActionException.class,
                    () -> game.drawFromDeck(player.getUsername(), DeckType.GOLDEN));
        }

        // test current
        // todo: check for correct deck type

        assertDoesNotThrow(() -> game.drawFromDeck(currentPlayer.getUsername(), DeckType.GOLDEN));

        // can draw only one card a time
        assertThrows(InvalidGamePhaseException.class,
                () -> game.drawFromDeck(currentPlayer.getUsername(), DeckType.RESOURCE));

        // after is invalid phase
        for (Player player : notCurrent) {
            assertThrows(InvalidGamePhaseException.class,
                    () -> game.drawFromDeck(player.getUsername(), DeckType.GOLDEN));
        }
    }
}
