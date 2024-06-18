package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.card.strategies.CalculateResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test to check the correct passage of the parameters in order to instance a front and whether two fronts match or not
 */
class FrontTest {
    private Map<CornerPosition, Corner> corners;

    private CalculatePoints calculator;

    /**
     * Instances a new corner with a resource and a <code>CalculateResource</code> calculator
     */
    @BeforeEach
    void setUp() {
        Corner generic_corner = new Corner(Symbol.ANIMAL);

        corners = new HashMap<>();

        corners.put(CornerPosition.LOWER_LEFT, new Corner(Symbol.FUNGI));

        calculator = new CalculateResources();
    }

    /**
     * Test to check that passing null corners throws an <code>IllegalArgumentException</code>
     */
    @Test
    public void passNullCorners_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(CardColor.BLUE, null, 0, calculator)
        );
    }

    /**
     * Test to check that passing null corner positions throws an <code>IllegalArgumentException</code>
     */
    @Test
    public void nullCornerPosition_throwsException() {
        corners.put(null, new Corner(Symbol.ANIMAL));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(CardColor.BLUE, corners, 0, calculator)
        );

        corners.remove(null);
    }

    /**
     * Test to check that passing a null corner inside the corners passed throws an <code>IllegalArgumentException</code>
     */
    @Test
    public void nullCornerInCorners_throwsException() {
        corners.put(CornerPosition.LOWER_LEFT, null);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(CardColor.BLUE, corners, 0, calculator)
        );
    }

    /**
     * Test to check that passing a negative score throws an <code>IllegalArgumentException</code>
     */
    @Test
    public void passNegativeScore_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(CardColor.BLUE, corners, -1, calculator)
        );
    }

    /**
     * Test to check that passing a null calculator throws an <code>IllegalArgumentException</code>
     */
    @Test
    public void passNullCalculator_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(CardColor.BLUE, corners, -1, null)
        );
    }

    /**
     * Test to check that passing correct parameters doesn't throw any kind of exception
     */
    @Test
    public void passCorrectParameters_doesNotThrow() {
        Assertions.assertDoesNotThrow(
                () -> {
                    new Front(CardColor.BLUE, corners, 0, calculator);
                }
        );
    }

    /**
     * Test to check if two fronts are the same depending on their attributes
     */
    @Test
    public void sameAttributes_equalsFronts() {
        Front f1 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                new CalculateResources()
        );

        Front f2 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                new CalculateResources()
        );

        Assertions.assertEquals(f1, f2);
    }

    /**
     * Test to check if two fronts are different depending on their <code>calculator</code>
     */
    @Test
    public void differentCalculators_differentFronts() {
        Front f1 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                new CalculateResources()
        );

        Front f2 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                new CalculateNoCondition()
        );

        Assertions.assertNotEquals(f1, f2);
    }

    /**
     * Test to check if two fronts are different depending on the constructor used
     */
    @Test
    public void differentDynamicType_differentFronts() {
        Front f1 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                new CalculateResources()
        );

        Front f2 = new GoldenFront(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                Condition.CORNERS,
                new CalculateNoCondition(),
                new HashMap<>()
        );

        Assertions.assertNotEquals(f1, f2);
    }

    @Test
    public void equalDynamicType_equalFronts() {

        Front f1 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                new CalculateResources()
        );

        Face f2 = new Front(
                CardColor.BLUE,
                new HashMap<>(),
                0,
                new CalculateResources()
        );

        Assertions.assertEquals(f1, f2);
    }
}
