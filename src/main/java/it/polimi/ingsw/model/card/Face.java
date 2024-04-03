package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;

import java.util.*;

import static java.util.Arrays.asList;

/**
 * Immutable class representing the Face card.
 * The face can be either the front or the back.
 */
abstract public class Face {
    private final Color color;

    //public static final int NUM_CORNERS = 4;
    private final Map<CornerPosition, Corner> corners;

    /**
     * Constructs a face card with the color and corners provided.
     *
     * @param color   of the card.
     * @param corners contains corners to be inserted as card's corner.
     * @throws IllegalArgumentException if any argument is null or <code>cornerList</code> has not 4 corners or any of them is null.
     */
    public Face(Color color, Map<CornerPosition, Corner> corners) throws IllegalArgumentException {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }

        if (corners == null) {
            throw new IllegalArgumentException("corners cannot be null");
        }

        if (corners.containsKey(null)) {
            throw new IllegalArgumentException("No null position for corners are allowed");
        }

        if (corners.containsValue(null)) {
            throw new IllegalArgumentException("No null Corner objects are allowed in corners");
        }

        this.color = color;

        this.corners = Collections.unmodifiableMap(corners);
    }

    public Color getColor() {
        return color;
    }

    public Map<CornerPosition, Corner> getCorners() {
        return corners;
    }

    /**
     * Returns face resources.
     *
     * @return empty map: by default a face has no resources.
     */
    public Map<Symbol, Integer> getResources() {
        return new HashMap<Symbol, Integer>();
    }

    /**
     * Returns required resources to place the card on the chosen side.
     *
     * @return empty map: by default a face has no required resources.
     */
    public Map<Symbol, Integer> getRequiredResources() {
        return new HashMap<Symbol, Integer>();
    }

    public int calcPoints(Playground p) {
        return 0;
    }
}
