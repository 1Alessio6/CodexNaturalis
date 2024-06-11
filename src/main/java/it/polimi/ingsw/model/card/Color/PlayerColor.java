package it.polimi.ingsw.model.card.Color;

import java.util.HashMap;
import java.util.Map;

public enum PlayerColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
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
