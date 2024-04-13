package it.polimi.ingsw.model.board;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaygroundTest {

    @Test
    void contains() {
        Playground p = new Playground();

    }

    @Test
    void getAllPositions() {
        Playground p = new Playground();
        assertTrue(p.getAllPositions().contains(new Position(0, 0)));
    }

    @Test
    void getAvailablePositions() {
    }

    @Test
    void placeCard() {

        //test place back




    }
}