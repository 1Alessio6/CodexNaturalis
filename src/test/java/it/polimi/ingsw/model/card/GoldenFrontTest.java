package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class GoldenFrontTest {
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
