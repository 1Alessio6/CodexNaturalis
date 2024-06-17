package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Color.CardColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * Test to check whether two back cards match or not, depending on their attributes.
 */
public class BackTest {
    /**
     * Test to check if two back cards are the same depending on their attributes.
     */
    @Test
    public void sameAttributes_sameBack() {
        Back b1 = new Back(CardColor.BLUE, new HashMap<>(), new HashMap<>());
        Back b2 = new Back(CardColor.BLUE, new HashMap<>(), new HashMap<>());

        Assertions.assertEquals(b1, b2);
    }

    /**
     * Test to check if two back cards are the different depending on their attributes.
     */
    @Test
    public void diffAttributes_differentBack() {
        Back b1 = new Back(CardColor.BLUE, new HashMap<>(), new HashMap<>());
        Back b2 = new Back(CardColor.GREEN, new HashMap<>(), new HashMap<>());

        Assertions.assertNotEquals(b1, b2);
    }
}
