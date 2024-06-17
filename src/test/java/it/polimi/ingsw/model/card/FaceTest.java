package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Color.CardColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * Test to check the correct differentiation between back and front faces
 */
public class FaceTest {
    /**
     * Test to check the differentiation between a back and a front card
     */
    @Test
    void backDifferentFromFront() {
        Face f1 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0
        );

        Face f2 = new Back(
                CardColor.BLUE,
                new HashMap<>(),
                new HashMap<>()
        );

        Assertions.assertNotEquals(f1, f2);
    }

    /**
     * Test to check the equality of two front cards if they have the same attributes
     */
    @Test
    void frontsWithSameAttributes_equalFronts() {

        Face f1 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0
        );

        Face f2 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0
        );

        Assertions.assertEquals(f1, f2);
    }

    /**
     * Test to check that two different front cards have different attributes
     */
    @Test
    void frontsWithDifferentAttributes_differentFronts() {

        Face f1 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0
        );

        Face f2 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                10
        );

        Assertions.assertNotEquals(f1, f2);
    }

    /**
     * Test to check the equality of two back cards if they have the same attributes
     */
    @Test
    void backsWithSameAttributes_equalBacks() {
        Face f1 = new Back(
                CardColor.BLUE,
                new HashMap<>(),
                new HashMap<>()
        );

        Face f2 = new Back(
                CardColor.BLUE,
                new HashMap<>(),
                new HashMap<>()
        );

        Assertions.assertEquals(f1, f2);
    }

    /**
     * Test to check that two different back cards have different attributes
     */
    @Test
    void backsWithDifferentAttributes_differentBacks() {

        Face f1 = new Back(
                CardColor.BLUE,
                new HashMap<>(),
                new HashMap<>()
        );

        Face f2 = new Back(
                CardColor.RED,
                new HashMap<>(),
                new HashMap<>()
        );

        Assertions.assertNotEquals(f1, f2);
    }

}

