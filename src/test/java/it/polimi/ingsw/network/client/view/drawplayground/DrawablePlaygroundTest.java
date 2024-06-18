package it.polimi.ingsw.network.client.view.drawplayground;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;
import it.polimi.ingsw.network.client.view.tui.drawplayground.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

class DrawablePlaygroundTest {
    Playground playground;
    Front sampleCard;

    @BeforeEach
    void setup(){
        playground = new Playground();

        Map<CornerPosition, Corner> corners = new HashMap<>();
        corners.put(CornerPosition.TOP_LEFT, new Corner());
        Front starterFront = new Front(CardColor.BLUE, corners, 0);
        Assertions.assertDoesNotThrow(
                () -> {
                    playground.placeCard(starterFront, new Position(0, 0));
                });

        Map<CornerPosition, Corner> cornersTwo = new HashMap<>();
        for (CornerPosition cp : CornerPosition.values()) {
            cornersTwo.put(cp, new Corner(Symbol.PLANT));
        }
        sampleCard = new Front(CardColor.GREEN, cornersTwo, 1);
    }

    //@AfterEach
    //void teardown() throws UndrawablePlaygroundException {
    //}

    @Test
    void assert_offsetWithoutOverflowThrows() throws UndrawablePlaygroundException {
        Assertions.assertDoesNotThrow(
                () -> {
                    playground.placeCard(sampleCard, new Position(-1, 1));
                });

        ClientPlayground clientPlayground = new ClientPlayground(playground);
        Assertions.assertThrows(UndrawablePlaygroundException.class,
                () -> ClientUtil.printPlayground(clientPlayground, new Position(0,0), new Position(-1, 1)));
    }

    @ParameterizedTest
    @MethodSource("inputsAndResults")
    void testOverflowingPlayground_withSaneOffset(Map.Entry<Position, Position> inputAndResult) throws UndrawablePlaygroundException {
        Position input = inputAndResult.getKey();
        Position result = inputAndResult.getValue();

        Assertions.assertDoesNotThrow(
                () -> {
                    for (int i = 1; i < 23; ++i) {
                        playground.placeCard(sampleCard, new Position(-i, i));
                    }
                });

        ClientPlayground clientPlayground = new ClientPlayground(playground);
        Assertions.assertEquals(result,
                ClientUtil.printPlayground(clientPlayground, new Position(0,0), input));
    }

    private static Stream<Map.Entry<Position, Position>> inputsAndResults() {
        Map<Position, Position> inputsAndResults = new HashMap<>();
        // sane offsets: input equals output
        inputsAndResults.put(new Position(0,0), new Position(0,0));
        inputsAndResults.put(new Position(1,0), new Position(1,0));
        inputsAndResults.put(new Position(-1,0), new Position(-1,0));
        inputsAndResults.put(new Position(0,1), new Position(0,1));
        inputsAndResults.put(new Position(0,-1), new Position(0,-1));
        inputsAndResults.put(new Position(-1,-1), new Position(-1,-1));
        inputsAndResults.put(new Position(1,1), new Position(1,1));
        inputsAndResults.put(new Position(-1,1), new Position(-1,1));
        inputsAndResults.put(new Position(1,-1), new Position(1,-1));

        // exaggerated offsets: normalize
        inputsAndResults.put(new Position(100,0), new Position(6,0));
        inputsAndResults.put(new Position(-100,0), new Position(-6,0));
        inputsAndResults.put(new Position(0, 100), new Position(0,2));
        inputsAndResults.put(new Position(0, -100), new Position(0,-2));
        inputsAndResults.put(new Position(-100,-100), new Position(-6,-2));
        inputsAndResults.put(new Position(100,100), new Position(6,2));
        inputsAndResults.put(new Position(-100,100), new Position(-6,2));
        inputsAndResults.put(new Position(100,-100), new Position(6,-2));
        return inputsAndResults.entrySet().stream();
    }
}