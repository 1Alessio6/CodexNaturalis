package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * GoldenFront is a class that extends Front and represents the front of a golden card
 */
public final class GoldenFront extends Front {
    /**
     * Represents the condition to be fulfilled.
     */
    private final Condition pointsCondition; /* should be enum? */

    /**
     * Represents a map with the resources and their corresponding amounts required to place the card.
     */
    private final Map<Symbol, Integer> requirements; /* set? */

    /**
     * Constructs a golden front card with no parameters provided.
     */
    public GoldenFront() {
        super();
        pointsCondition = Condition.CORNERS;
        requirements = new HashMap<>();
    }


    /**
     * Constructs a front card with the color, score and corners provided.
     *
     * @param color   of the card.
     * @param corners of the card.
     * @param score   obtained after positioning the card.
     * @param pointsCondition that trigger the calculator
     * @param calculator the logic of calculate points
     * @param requirements needed before playing the card
     * @throws IllegalArgumentException if any argument is null or there are not 4 corners or the score is negative.
     */
    public GoldenFront(CardColor color, Map<CornerPosition, Corner> corners, int score, Condition pointsCondition, CalculatePoints calculator, Map<Symbol, Integer> requirements) throws IllegalArgumentException {
        super(color, corners, score, calculator);
        this.pointsCondition = pointsCondition;
        this.requirements = requirements;
    }

    /**
     * Returns golden front required resources.
     *
     * @return a map containing the resources required by the golden front and their quantity.
     */
    public Map<Symbol, Integer> getRequiredResources() {
        return requirements;
    }

    /**
     * Returns the required condition
     * @return the required condition in order to get the points
     */
    public Condition getPointsCondition() {
        return pointsCondition;
    }

    @Override
    public Condition getCondition() {
        return this.pointsCondition;
    }

    /**
     * Checks if two objects are equal, two golden fronts in particular
     *
     * @param o the object to be compared
     * @return true if this object is equal to <code>o</code>, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GoldenFront that = (GoldenFront) o;
        return pointsCondition == that.pointsCondition && Objects.equals(requirements, that.requirements)
                    &&
               super.equals(o);
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
        return Objects.hash(super.hashCode(), pointsCondition, requirements);
    }
}
