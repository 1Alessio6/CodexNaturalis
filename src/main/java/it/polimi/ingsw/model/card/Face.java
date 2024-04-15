package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Immutable class representing the Face card.
 * The face can be either the front or the back.
 */
abstract public class Face {
    private final Color color;

    private final CalculatePoints calculator;

    // public static final int NUM_CORNERS = 4;
    private final Map<CornerPosition, Corner> corners;

    // representation invariant
    private boolean isValid(Map<CornerPosition, Corner> corners, CalculatePoints calculator) {
        return corners != null
                && calculator != null
                && !corners.containsKey(null)
                && !corners.containsValue(null);
    }

    /**
     * Constructs a face card with the color and corners provided.
     *
     * @param color   the face's color. If null, it represents a face without a color, for example a starting card.
     * @param corners contains corners to be inserted as card's corner.
     * @throws IllegalArgumentException if <code>cornerList</code> has not 4 corners or
     *                                  any of them is null.
     *                                  or calculator is null.
     */
    public Face(Color color, Map<CornerPosition, Corner> corners, CalculatePoints calculator)
            throws IllegalArgumentException {

        if (!isValid(corners, calculator)) {
            throw new IllegalArgumentException("Illegal argument passed to the constructor");
        }

        this.color = color;

        this.corners = Collections.unmodifiableMap(corners);

        this.calculator = calculator;
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
     * @return a map containing the resources of the face and their quantity.
     */
    public Map<Symbol, Integer> getResources() {
        return corners.values().stream().map(Corner::getSymbol)
                // group by symbol and increment by 1 for each occurrence
                .collect(Collectors.groupingBy(s -> s, HashMap::new, Collectors.summingInt(s -> 1)));
    }

    /**
     * Returns required resources to place the card on the chosen side.
     *
     * @return empty map: by default a face has no required resources.
     */
    public Map<Symbol, Integer> getRequiredResources() {
        return new HashMap<>();
    }

    public int calculatePoints(Position pos, Playground playground) {
        return calculator.calculatePoints(pos, playground);
    }

    public abstract int getScore();
}
