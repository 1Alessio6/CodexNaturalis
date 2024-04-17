package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class FaceTest {
    @Test
    void backDifferentFromFront() {
        Face f1 = new Front(
                Color.BLUE,
                new HashMap<>(),
                0
        );

        Face f2 = new Back(
                Color.BLUE,
                new HashMap<>(),
                new HashMap<>()
        );

        Assertions.assertNotEquals(f1, f2);
    }

    @Test
    void frontsWithSameAttributes_equalFronts() {

        Face f1 = new Front(
                Color.BLUE,
                new HashMap<>(),
                0
        );

        Face f2 = new Front(
                Color.BLUE,
                new HashMap<>(),
                0
        );

        Assertions.assertEquals(f1, f2);
    }

    @Test
    void frontsWithDifferentAttributes_differentFronts() {

        Face f1 = new Front(
                Color.BLUE,
                new HashMap<>(),
                0
        );

        Face f2 = new Front(
                Color.BLUE,
                new HashMap<>(),
                10
        );

        Assertions.assertNotEquals(f1, f2);
    }


    @Test
    void backsWithSameAttributes_equalBacks() {
        Face f1 = new Back(
                Color.BLUE,
                new HashMap<>(),
                new HashMap<>()
        );

        Face f2 = new Back(
                Color.BLUE,
                new HashMap<>(),
                new HashMap<>()
        );

        Assertions.assertEquals(f1, f2);
    }

    @Test
    void backsWithDifferentAttributes_differentBacks() {

        Face f1 = new Back(
                Color.BLUE,
                new HashMap<>(),
                new HashMap<>()
        );

        Face f2 = new Back(
                Color.RED,
                new HashMap<>(),
                new HashMap<>()
        );

        Assertions.assertNotEquals(f1, f2);
    }

}

