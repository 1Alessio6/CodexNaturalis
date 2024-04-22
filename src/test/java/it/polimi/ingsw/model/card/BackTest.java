package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class BackTest {
    @Test
    public void sameAttributes_sameBack() {
        Back b1 = new Back(Color.BLUE, new HashMap<>(), new HashMap<>());
        Back b2 = new Back(Color.BLUE, new HashMap<>(), new HashMap<>());

        Assertions.assertEquals(b1, b2);
    }

    @Test
    public void diffAttributes_differentBack() {
        Back b1 = new Back(Color.BLUE, new HashMap<>(), new HashMap<>());
        Back b2 = new Back(Color.GREEN, new HashMap<>(), new HashMap<>());

        Assertions.assertNotEquals(b1, b2);
    }
}
