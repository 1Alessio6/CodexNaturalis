package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.util.*;

/**
 * Immutable class representing the Face card.
 * The face can be either the front or the back.
 */
abstract public class Face {
    private final CardColor color;

    private final CalculatePoints calculator;

    private final Map<CornerPosition, Corner> corners;

    // id to represent a card unambiguously.
    private int id;

    private static int incrementalId = 0;

    private static int getIncrementalId() {
        return ++incrementalId;
    }


    // representation invariant
    private boolean isValid(Map<CornerPosition, Corner> corners, CalculatePoints calculator) {
        return corners != null
                && calculator != null
                && !corners.containsKey(null)
                && !corners.containsValue(null);
    }

    /**
     * Constructs a face card with no parameters provided.
     */
    public Face() {
        id = getIncrementalId();
        color = null;
        calculator = null;
        corners = null;
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
    public Face(CardColor color, Map<CornerPosition, Corner> corners, CalculatePoints calculator)
            throws IllegalArgumentException {

        if (!isValid(corners, calculator)) {
            throw new IllegalArgumentException("Illegal argument passed to the constructor");
        }

        this.color = color;

        this.corners = Collections.unmodifiableMap(corners);

        this.calculator = calculator;
    }

    public int getId() {
        return id;
    }

    public CardColor getColor() {
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
        Map<Symbol, Integer> resources = new HashMap<>();
        for (CornerPosition c : this.getCorners().keySet()) {
            Symbol s = this.getCorners().get(c).getSymbol();

            if (s != null) {
                if (resources.containsKey(s)) {
                    resources.put(s, resources.get(s) + 1);
                } else {
                    resources.put(s, 1);
                }
            }
        }

        return resources;
    }

    /**
     * Returns required resources to place the card on the chosen side.
     *
     * @return empty map: by default a face has no required resources.
     */
    public Map<Symbol, Integer> getRequiredResources() {
        return new HashMap<>();
    }

    /**
     * Calculates the points earned after the placement of a card in the <code>playground</code>.
     *
     * @param pos        the position in the playground
     * @param playground the playground on which the calculation is to be carried
     * @return the calculated points
     */
    public int calculatePoints(Position pos, Playground playground) {
        return calculator.calculatePoints(pos, playground);
    }

    public abstract int getScore();

    public abstract Condition getCondition();

    public Map<Symbol, Integer> getBackCenterResources(){
        return new HashMap<>();
    }

    /**
     * Checks if two objects are equal, two faces in particular
     *
     * @param o the object to be compared
     * @return true if the object is equal to <code>o</code>, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Face face = (Face) o;
        return color == face.color
                && calculator.equals(face.calculator)
                && corners.equals(face.corners);
    }

    /**
     * Returns a hashcode depending on the attributes.
     * Given two instance with the same attributes this method returns the same hashcode.
     * Overrides the hashCode() method in the Object class.
     *
     * @return an int value representing the hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(color, calculator, corners);
    }

}
