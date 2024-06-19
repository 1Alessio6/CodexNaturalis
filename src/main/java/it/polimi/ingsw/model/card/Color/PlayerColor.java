package it.polimi.ingsw.model.card.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration representing the possible token colors of the players
 */
public enum PlayerColor {

    /**
     * RED, stands for the red token color
     */
    RED,

    /**
     * BLUE, stands for the blue token color
     */
    BLUE,

    /**
     * GREEN, stands for the green token color
     */
    GREEN,

    /**
     * YELLOW, stands for the yellow token color
     */
    YELLOW,

    /**
     * BLACK, stands for the black token color
     */
    BLACK;

    public static final Map<PlayerColor, String> conversionToCssStyle;

    static {
        conversionToCssStyle = new HashMap<>();
        conversionToCssStyle.put(RED, "red");
        conversionToCssStyle.put(BLUE, "blue");
        conversionToCssStyle.put(GREEN, "green");
        conversionToCssStyle.put(YELLOW, "yellow");
    }
}
