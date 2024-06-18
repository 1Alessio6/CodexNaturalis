package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to check the correct operation of the <code>calculatePoints</code> method when a card with corner condition is
 * place
 */
class CalculateCornersTest {
    private Playground playground;
    private Front cornerFront;

    /**
     * Creates a new playground and places different cards with corner condition there before each test
     *
     * @throws Playground.UnavailablePositionException if the position is not available
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card
     */
    @BeforeEach
    void setUp() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        playground = new Playground();
        CardColor color = CardColor.RED;
        Map<CornerPosition, Corner> corners = new HashMap<>();
        Arrays.stream(CornerPosition.values()).forEach(cp -> corners.put(cp, new Corner()));
        int score = 2;

        // Place the same front, just as a setup
        cornerFront = new GoldenFront(color, corners, score, Condition.CORNERS, new CalculateCorners(), new HashMap<>());

        playground.placeCard(cornerFront, new Position(0,0));
        playground.placeCard(cornerFront, new Position(1,1));
        playground.placeCard(cornerFront, new Position(-1,-1));
        playground.placeCard(cornerFront, new Position(1,-1));
        playground.placeCard(cornerFront, new Position(2,2));
        playground.placeCard(cornerFront, new Position(2,-2));
        playground.placeCard(cornerFront, new Position(1,-3));
        playground.placeCard(cornerFront, new Position(3,1));
        playground.placeCard(cornerFront, new Position(3,-1));
    }

    /**
     * Tests if calculatePoints method calculates correctly the earned points in the case of a corner being covered
     * by the card with corner condition
     *
     * @throws Playground.UnavailablePositionException if the position is not available
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card
     */
    @Test
    void coverOneCorner() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        Position myPos = new Position(-1, 1);

        playground.placeCard(cornerFront, myPos);
        assertEquals(2, cornerFront.calculatePoints(myPos, playground));
    }

    /**
     * Tests if calculatePoints method calculates correctly the earned points in the case of 2 corners being covered
     * by the card with corner condition
     *
     * @throws Playground.UnavailablePositionException if the position is not available
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card
     */
    @Test
    void coverTwoCorners() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        Position myPos = new Position(4,0);

        playground.placeCard(cornerFront, myPos);
        assertEquals(4, cornerFront.calculatePoints(myPos, playground));
    }

    /**
     * Tests if calculatePoints method calculates correctly the earned points in the case of 3 corners being covered
     * by the card with corner condition
     *
     * @throws Playground.UnavailablePositionException if the position is not available
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card
     */
    @Test
    void coverThreeCorners() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        Position myPos = new Position(0, -2);

        playground.placeCard(cornerFront, myPos);
        assertEquals(6, cornerFront.calculatePoints(myPos, playground));
    }

    /**
     * Tests of calculatePoints method calculates correctly the earned points in the case of 4 corners being covered
     * by the card with corner condition
     *
     * @throws Playground.UnavailablePositionException if the position is not available
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card
     */
    @Test
    void coverFourCorners() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        Position myPos = new Position(2,0);

        playground.placeCard(cornerFront, myPos);
        assertEquals(8, cornerFront.calculatePoints(myPos, playground));
    }
}