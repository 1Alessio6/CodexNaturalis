package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.CardColor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ObjectivePositionCardTest {
    static Playground playground;
    static Front red;
    static Front blue;
    static Front green;

    static ObjectivePositionCard lForm;
    static ObjectivePositionCard diagonalForm;

    @BeforeAll
    static void beforeAll() {
        HashMap<CornerPosition, Corner> corners = new HashMap<>();
        for (CornerPosition position : CornerPosition.values()) {
            corners.put(position, new Corner());
        }

        red = new Front(CardColor.RED, corners, 0);
        green = new Front(CardColor.GREEN, corners, 0);
        blue = new Front(CardColor.BLUE, corners, 0);

        HashMap<Position, CardColor> lPos = new HashMap<>();
        lPos.put(new Position(0,0), CardColor.RED);
        lPos.put(new Position(-1,1), CardColor.GREEN);
        lPos.put(new Position(-1,3), CardColor.GREEN);


        HashMap<Position, CardColor> diagonalPos = new HashMap<>();
        diagonalPos.put(new Position(0,0), CardColor.RED);
        diagonalPos.put(new Position(1,1), CardColor.RED);
        diagonalPos.put(new Position(2,2), CardColor.RED);

        playground = new Playground();
        assertDoesNotThrow(() -> playground.placeCard(blue, new Position(0,0)));

        diagonalForm = new ObjectivePositionCard(diagonalPos, 1);
        lForm = new ObjectivePositionCard(lPos, 1);
    }

    @Test
    void matchRequirements() {
    }

    @Test
    void calculateDiagonalPoints() {
        assertDoesNotThrow(() -> playground.placeCard(red, new Position(1,1)));
        assertDoesNotThrow(() -> playground.placeCard(red, new Position(2,2)));
        assertDoesNotThrow(() -> playground.placeCard(red, new Position(3,3)));
        assertEquals(1, diagonalForm.calculatePoints(playground));

        assertDoesNotThrow(() -> playground.placeCard(red, new Position(4,4)));
        assertNotEquals(2, diagonalForm.calculatePoints(playground));

        assertDoesNotThrow(() -> playground.placeCard(red, new Position(5,5)));
        assertNotEquals(3, diagonalForm.calculatePoints(playground));

        assertDoesNotThrow(() -> playground.placeCard(red, new Position(6,6)));
        assertNotEquals(4, diagonalForm.calculatePoints(playground));
        assertEquals(2, diagonalForm.calculatePoints(playground));
    }

    @Test
    void calculateLPosition() {
        assertDoesNotThrow(() -> playground.placeCard(red, new Position(1,1)));
        assertDoesNotThrow(() -> playground.placeCard(green, new Position(0,2)));
        assertDoesNotThrow(() -> playground.placeCard(red, new Position(1, 3)));
        assertDoesNotThrow(() -> playground.placeCard(green, new Position(0,4)));
        assertEquals(1, lForm.calculatePoints(playground));

        assertDoesNotThrow(() -> playground.placeCard(red, new Position(1, 5)));
        assertDoesNotThrow(() -> playground.placeCard(green, new Position(0,6)));
        assertNotEquals(2, lForm.calculatePoints(playground));

        assertDoesNotThrow(() -> playground.placeCard(red, new Position(1, 7)));
        assertDoesNotThrow(() -> playground.placeCard(green, new Position(0,8)));
        assertEquals(2, lForm.calculatePoints(playground));
    }

    @Test
    void sameCardForMoreLayouts() {
        assertDoesNotThrow(() -> playground.placeCard(red, new Position(1,1)));
        assertDoesNotThrow(() -> playground.placeCard(red, new Position(2,2)));
        assertDoesNotThrow(() -> playground.placeCard(red, new Position(3,3)));
        assertDoesNotThrow(() -> playground.placeCard(green, new Position(0,2)));
        assertDoesNotThrow(() -> playground.placeCard(red, new Position(1, 3)));
        assertDoesNotThrow(() -> playground.placeCard(green, new Position(0,4)));


        assertEquals(1, lForm.calculatePoints(playground));
        assertEquals(1, diagonalForm.calculatePoints(playground));
    }
}