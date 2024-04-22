package it.polimi.ingsw.model.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    @Test
    void sum() {

        Position p1 = new Position(4,1);
        Position p2 = new Position(3,4);
        Position result1 = Position.sum(p1,p2);
        Assertions.assertEquals(result1, new Position(7,5));

        Position p3 = new Position(-2,-1);
        Position p4 = new Position(3,-4);
        Position result2 = Position.sum(p3,p4);
        Assertions.assertEquals(result2, new Position(1,-5));

        Position p5 = new Position(99,94);
        Position p6 = new Position(-399,-4);
        Position result3 = Position.sum(p5,p6);
        Assertions.assertEquals(result3, new Position(-300,90));

        Position p7 = new Position(0,0);
        Position p8 = new Position(3,-4);
        Position result4 = Position.sum(p7,p8);
        Assertions.assertEquals(result4, new Position(3,-4));
    }
}