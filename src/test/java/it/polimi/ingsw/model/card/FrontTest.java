package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.card.strategies.CalculateResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FrontTest {
    private Map<CornerPosition, Corner> corners;

    private CalculatePoints calculator;

    @BeforeEach
    void setUp() {
        Corner generic_corner = new Corner(Symbol.ANIMAL);

        corners = new HashMap<>();

        corners.put(CornerPosition.LOWER_LEFT, new Corner(Symbol.FUNGI));

        calculator = new CalculateResources();
    }

    @Test
    public void passNullColor_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(null, corners, 0, calculator)
        );
    }

    @Test
    public void passNullCorners_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, null, 0, calculator)
        );
    }

    @Test
    public void nullCornerPosition_throwsException() {
        corners.put(null, new Corner(Symbol.ANIMAL));

         Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, corners, 0, calculator)
        );

         corners.remove(null);
    }

    @Test
    public void nullCornerInCorners_throwsException() {
        corners.put(CornerPosition.LOWER_LEFT, null);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, corners, 0, calculator)
        );
    }


    @Test
    public void passNegativeScore_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, corners, -1, calculator)
        );
    }

    @Test
    public void passNullCalculator_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, corners, -1, null)
        );
    }

    @Test
    public void passCorrectParameters_doesNotThrow() {
        Assertions.assertDoesNotThrow(
                () -> {
                    new Front(Color.BLUE, corners, 0, calculator);
                }
        );
    }
}
