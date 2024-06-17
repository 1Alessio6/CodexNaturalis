package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * Test to check whether two golden front cards match or not, depending on their attributes.
 */
public class GoldenFrontTest {
    /**
     * Test to check if two golden front card are the same depending on their attributes
     */
    @Test
    void sameAttributes_equalGoldenFronts() {
        GoldenFront gf1 = new GoldenFront(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                Condition.CORNERS,
                new CalculateNoCondition(),
                new HashMap<>()
        );

        GoldenFront gf2 = new GoldenFront(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                Condition.CORNERS,
                new CalculateNoCondition(),
                new HashMap<>()
        );

        Assertions.assertEquals(gf1, gf2);
    }

    /**
     * Test to check if two golden front cards are the different depending on their attributes, including non-null
     * corners
     */
    @Test
    void differentBaseClassAttributes_differentGoldenFronts() {
        GoldenFront gf1 = new GoldenFront(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                Condition.CORNERS,
                new CalculateNoCondition(),
                new HashMap<>()
        );


        HashMap<CornerPosition, Corner> corners = new HashMap<>();
        corners.put(CornerPosition.LOWER_LEFT, new Corner());

        GoldenFront gf2 = new GoldenFront(
                CardColor.BLUE,
                corners,
                0,
                Condition.CORNERS,
                new CalculateNoCondition(),
                new HashMap<>()
        );

        Assertions.assertNotEquals(gf1, gf2);
    }

    /**
     * Test to check if two golden front cards are different depending on their attributes, including non-null resources
     */
    @Test
    void differentSuperClassAttributes_differentGoldenFronts() {
        GoldenFront gf1 = new GoldenFront(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                Condition.CORNERS,
                new CalculateNoCondition(),
                new HashMap<>()
        );


        HashMap<Symbol, Integer> res = new HashMap<>();
        res.put(Symbol.ANIMAL, 10);

        GoldenFront gf2 = new GoldenFront(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                Condition.CORNERS,
                new CalculateNoCondition(),
                res
        );

        Assertions.assertNotEquals(gf1, gf2);
    }

}
