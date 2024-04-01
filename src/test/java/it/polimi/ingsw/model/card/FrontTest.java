package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class FrontTest {
    private List<Corner> cornerList;

    @BeforeEach
    void setUp() {
        Corner generic_corner = new Corner(Symbol.ANIMAL);

        cornerList = new ArrayList<>();

        for (int i = 0; i < Face.NUM_CORNERS; ++i) {
            cornerList.add(generic_corner);
        }
    }

    @Test
    public void passNullColor_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(null, cornerList, 0)
        );
    }

    @Test
    public void passNullCorners_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, null, 0)
        );
    }


    @Test
    public void nullCornerInCornerList_throwsException() {
        cornerList.set(0, null);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, cornerList, 0)
        );
    }

    @Test
    public void passLongerCornerList_throwsException() {
        cornerList.add(new Corner(Symbol.FUNGI));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, cornerList, 0)
        );
    }

    @Test
    public void passSmallerCornerList_throwsException() {
        cornerList.remove(0);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, cornerList, 0)
        );
    }

    @Test
    public void passNegativeScore_throwsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Front(Color.BLUE, cornerList, -1)
        );
    }

    @Test
    public void passCorrectParameters_doesNotThrow() {
        Assertions.assertDoesNotThrow(
                () -> {
                    new Front(Color.BLUE, cornerList, 0);
                }
        );
    }
}
