package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.color.CardColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable class representing the objective card.
 */
public abstract class ObjectiveCard{

    /**
     * Represents the number of points won when card is placed.
     */
    int multiplier;

    private int frontId;

    private int backId;

    // in total 16 front and 1 back
    private static final int COMMON_BACK_ID = 17;

    private static int incrementalId = 0;

    private static int getIncrementalId() {
        return ++incrementalId;
    }

    /**
     * Constructs an objective card with no parameters provided.
     */
    public ObjectiveCard() {
        frontId = getIncrementalId();
        backId = COMMON_BACK_ID;
    }

    /**
     * Constructs an objective card with given provided.
     *
     * @param multiplier of the objective card
     */
    public ObjectiveCard(int multiplier) {
        this.multiplier = multiplier;
        this.frontId = getIncrementalId();
        this.backId = getIncrementalId();
    }

    public void getObjective() {
    }

    /**
     * Returns the multiplier in the objective card
     *
     * @return the multiplier
     */
    public int getMultiplier() {
        return multiplier;
    }

    /**
     * Calculates the points earned by fulfilling the condition present in the objective card
     *
     * @param p refers to the playground in which the calculation is performed
     * @return the number of calculated points
     */
    public abstract int calculatePoints(Playground p);


    public int getFrontId() {
        return frontId;
    }

    public int getBackId() {
        return backId;
    }

    /**
     * Returns a map indicating the resources required to earn points in the event that the card in question is an objective resource card
     *
     * @return resource map
     */
    public Map<Symbol, Integer> getResourceCondition() {
        return new HashMap<>();
    }

    /**
     * Returns a map indicating the positions that cards should have in order to earn points in the objective position card case
     *
     * @return a map with the position and colors of the cards
     */
    public Map<Position, CardColor> getPositionCondition() {
        return new HashMap<>();
    }

    /**
     * Checks if two objects are equal, two objective cards in particular.
     *
     * @param o the object to be compared
     * @return true if this object is equal to <code>o</code>, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectiveCard that = (ObjectiveCard) o;
        return multiplier == that.multiplier;
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
        return Objects.hashCode(multiplier);
    }
}
