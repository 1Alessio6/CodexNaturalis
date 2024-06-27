package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.color.CardColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to check the correct operation of the <code>calculatePoints</code> method when a card with resource condition is
 * place
 */
class CalculateResourcesTest {
    private Playground playground;
    private Front resourceFront;
    private int countResources;
    private Condition condition;

    /**
     * Creates a new playground and place a resource front there before each test
     *
     * @throws Playground.UnavailablePositionException if the position is not available
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card
     */
    @BeforeEach
    void setUp() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        playground = new Playground();
        CardColor color = CardColor.RED;
        condition = Condition.NUM_QUILL;
        Map<CornerPosition, Corner> corners = new HashMap<>();
        corners.put(CornerPosition.LOWER_LEFT, new Corner());
        corners.put(CornerPosition.LOWER_RIGHT, new Corner());
        corners.put(CornerPosition.TOP_LEFT, new Corner());
        corners.put(CornerPosition.TOP_RIGHT, new Corner(CalculateResources.conditionSymbolConversion(condition)));
        int score = 1;

        resourceFront = new GoldenFront(color, corners, score, condition, new CalculateResources(), new HashMap<>());

        playground.placeCard(resourceFront, new Position(0,0));
    }

    /**
     * Tests if calculatePoints method calculates correctly the earned points in the case of a resource card covering
     * the only resource of another card
     *
     * @throws Playground.UnavailablePositionException if the position is not available
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card
     */
    @Test
    void coverResource() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        countResources = playground.getResources().get(CalculateResources.conditionSymbolConversion(condition));
        Position mypos = new Position(1, 1);
        playground.placeCard(resourceFront, mypos);

        // resources before the card placement are the same
        assertEquals(countResources, resourceFront.calculatePoints(mypos, playground));
    }

    /**
     * Tests if calculatePoints method calculates correctly the earned points in the case of another resource card placed
     * in the playground without covering the only resource of the initial card
     *
     * @throws Playground.UnavailablePositionException if the position is not available
     * @throws Playground.NotEnoughResourcesException  if the player's resource re not enough to place the card
     */
    @Test
    void noCoverResource() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        countResources = playground.getResources().get(CalculateResources.conditionSymbolConversion(condition));
        Position mypos = new Position(-1, -1);
        playground.placeCard(resourceFront, mypos);

        // resources before the card placement are one less than now
        assertEquals(countResources + 1 , resourceFront.calculatePoints(mypos, playground));
    }
}