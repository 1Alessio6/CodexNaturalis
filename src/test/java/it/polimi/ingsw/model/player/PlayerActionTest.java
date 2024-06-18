package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.player.action.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to check the correct functioning of the <code>PlayerAction</code>
 */
class PlayerActionTest {
    Player player;

    /**
     * Instances a new player before each test
     */
    @BeforeEach
    void setUp() {
        CardColor color = CardColor.BLUE;
        Map<CornerPosition, Corner> corners = new HashMap<>();
        Arrays.stream(CornerPosition.values()).forEach(cp -> corners.put(cp, new Corner()));

        Front front = new Front(color, corners, 0);
        Back back = new Back(color, corners, new HashMap<>());
        Card myCard = new Card(front, back);

        // generic list of same cards: cards' details aren't important in these tests
        List<Card> cards = new ArrayList<>();
        cards.add(myCard);
        cards.add(myCard);
        cards.add(myCard);

        List<ObjectiveCard> objectiveCards = new ArrayList<>();
        Map<Symbol, Integer> resourceCondition = new HashMap<>();
        resourceCondition.put(Symbol.FUNGI, 3);
        resourceCondition.put(Symbol.ANIMAL, 3);
        ObjectiveCard objectiveCard = new ObjectiveResourceCard(resourceCondition, 3);
        objectiveCards.add(objectiveCard);
        objectiveCards.add(objectiveCard);

        player = new Player("pippo", myCard, cards, objectiveCards);
    }

    /**
     * Test to check that a placement can occur only in the <code>Place</code> and the <code>ChooseStarter</code> player
     * actions
     *
     * @param playerAction the current player action of the player
     */
    @ParameterizedTest
    @ArgumentsSource(PlayerActionProvider.class)
    void placeCard(PlayerAction playerAction) {
        int cardIdx = 2;
        Card myCard = player.getCards().get(cardIdx);
        Side mySide = Side.FRONT;
        // correctness of placement already tested in PlaygroundTest
        // position is first: no cards are placed before
        Position myPos = new Position(0,0);

        // test all actions
        player.setAction(playerAction);

        if (playerAction.getClass().equals(ChooseStarter.class)) {
            assertDoesNotThrow(() -> player.placeStarter(mySide));
        } else if (playerAction.getClass().equals(Place.class)){
            assertDoesNotThrow(() -> player.placeCard(myCard, mySide, myPos));
            assertEquals(2, player.getCards().size());
        } else {
            assertThrows(InvalidPlayerActionException.class, () -> player.placeCard(myCard, mySide, myPos));
        }
    }

    /**
     * Test to check that the placement of the objective card can occur only in the <code>ChooseObjective</code> player
     * action
     *
     * @param playerAction the current player action of the player
     */
    @ParameterizedTest
    @ArgumentsSource(PlayerActionProvider.class)
    void placeObjectiveCardTest(PlayerAction playerAction) {
        int objectiveIdx = 0;
        ObjectiveCard selectedObjective = player.getObjectives().get(objectiveIdx);
        // test all actions
        player.setAction(playerAction);

        if (!playerAction.getClass().equals(ChooseObjective.class)) {
            assertThrows(InvalidPlayerActionException.class, () -> player.placeObjectiveCard(objectiveIdx));
        } else {
            assertDoesNotThrow(() -> player.placeObjectiveCard(objectiveIdx));
            assertTrue(player.getObjectives().size() == 1 && player.getObjectives().contains(selectedObjective));
        }
    }

    /**
     * Test to check that a draw can occur only in the <code>Draw</code> player action
     *
     * @param playerAction the current player action of the player
     */
    @ParameterizedTest
    @ArgumentsSource(PlayerActionProvider.class)
    void addCard(PlayerAction playerAction) {
        // test all actions
        player.setAction(playerAction);

        //random card to add to player's hand
        Card myCard = player.getCards().getFirst();
        if (playerAction.getPlayerState()==PlayerState.Draw) {
            // impossible situation to draw with 3 cards
            player.getCards().removeFirst();

            assertDoesNotThrow(() -> player.addCard(myCard));
            assertEquals(3, player.getCards().size());
        } else {
            assertThrows(InvalidPlayerActionException.class, () -> player.addCard(myCard));
        }
    }
}

/**
 * PlayerActionProvider represents the class that provides Player Actions
 */
class PlayerActionProvider implements ArgumentsProvider {
    /**
     * Provides a stream of Player actions
     *
     * @param context the extension context
     * @return the stream of Player actions
     */
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(new ChooseObjective(), new ChooseStarter(), new Draw(), new Place()).map(Arguments::of);
    }
}
