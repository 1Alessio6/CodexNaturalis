package it.polimi.ingsw.network.client.view.drawplayground;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.color.CardColor;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;
import it.polimi.ingsw.network.client.view.tui.drawplayground.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Test to check the correct functioning of the <code>DrawablePlayground</code> class
 */
class DrawablePlaygroundTest {
    Playground playground;
    Front sampleCard;
    Position currentOffset;

    /**
     * Creates a new playground and placed a starter card on it, in addition, it creates a sample card before each test
     */
    @BeforeEach
    void setup(){
        playground = new Playground();
        currentOffset = new Position(0, 0);

        Map<CornerPosition, Corner> allCorners = new HashMap<>();
        for (CornerPosition cp : CornerPosition.values()) {
            allCorners.put(cp, new Corner(Symbol.PLANT));
        }
        Front starterFront = new Front(CardColor.BLUE, allCorners, 0);
        Assertions.assertDoesNotThrow(
                () -> {
                    playground.placeCard(starterFront, new Position(0, 0));
                });

        sampleCard = new Front(CardColor.GREEN, allCorners, 1);
    }

    //@AfterEach
    //void teardown() throws UndrawablePlaygroundException {
    //}

    /**
     * Test to check that an <code>UndrawablePlaygroundException</code> is correctly thrown when the player attempts to
     * move into a specific offset but the playground is fully represented
     *
     */
    @Test
    void assert_offsetWithoutOverflowThrows() {
        Assertions.assertDoesNotThrow(
                () -> {
                    playground.placeCard(sampleCard, new Position(1, 1));
                });

        ClientPlayground clientPlayground = new ClientPlayground(playground);

        Assertions.assertDoesNotThrow(() -> ClientUtil.printPlayground(clientPlayground, currentOffset));
        Assertions.assertThrows(UndrawablePlaygroundException.class,
                () -> ClientUtil.printPlayground(clientPlayground, currentOffset, new Position(-1, 1)));
    }

    /**
     * Test to check that the positions returned by the <code>printPlayground</code> are correct
     *
     * @param inputAndResult the incoming and expected positions
     * @throws UndrawablePlaygroundException if an error occurs during the playground representation design
     */
    @ParameterizedTest
    @MethodSource("inputsAndResults")
    void testOverflowingPlayground_withSaneOffset(Map.Entry<Position, Position> inputAndResult) throws UndrawablePlaygroundException {
        Position input = inputAndResult.getKey();
        Position result = inputAndResult.getValue();

        Assertions.assertDoesNotThrow(
                () -> {
                    for (int i = 1; i < 22; ++i) {
                        playground.placeCard(sampleCard, new Position(-i, i));
                    }
                });

        ClientPlayground clientPlayground = new ClientPlayground(playground);
        Assertions.assertEquals(result,
                ClientUtil.printPlayground(clientPlayground, currentOffset, input));
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