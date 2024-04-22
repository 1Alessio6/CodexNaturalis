package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CalculateCornersTest {
    private Playground playground;
    private Front cornerFront;

    @BeforeEach
    void setUp() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        playground = new Playground();
        Color color = Color.RED;
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

    @Test
    void coverOneCorner() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        Position myPos = new Position(-1, 1);

        playground.placeCard(cornerFront, myPos);
        assertEquals(2, cornerFront.calculatePoints(myPos, playground));
    }

    @Test
    void coverTwoCorners() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        Position myPos = new Position(4,0);

        playground.placeCard(cornerFront, myPos);
        assertEquals(4, cornerFront.calculatePoints(myPos, playground));
    }

    @Test
    void coverThreeCorners() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        Position myPos = new Position(0, -2);

        playground.placeCard(cornerFront, myPos);
        assertEquals(6, cornerFront.calculatePoints(myPos, playground));
    }

    @Test
    void coverFourCorners() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        Position myPos = new Position(2,0);

        playground.placeCard(cornerFront, myPos);
        assertEquals(8, cornerFront.calculatePoints(myPos, playground));
    }
}