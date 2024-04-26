package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateResourcesTest {
    private Playground playground;
    private Front resourceFront;
    private int countResources;
    private Condition condition;

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

    @Test
    void coverResource() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        countResources = playground.getResources().get(CalculateResources.conditionSymbolConversion(condition));
        Position mypos = new Position(1, 1);
        playground.placeCard(resourceFront, mypos);

        // resources before the card placement are the same
        assertEquals(countResources, resourceFront.calculatePoints(mypos, playground));
    }

    @Test
    void noCoverResource() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        countResources = playground.getResources().get(CalculateResources.conditionSymbolConversion(condition));
        Position mypos = new Position(-1, -1);
        playground.placeCard(resourceFront, mypos);

        // resources before the card placement are one less than now
        assertEquals(countResources + 1 , resourceFront.calculatePoints(mypos, playground));
    }
}