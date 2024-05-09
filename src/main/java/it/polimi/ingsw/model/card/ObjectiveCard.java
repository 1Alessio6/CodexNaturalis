package it.polimi.ingsw.model.card;

import it.polimi.ingsw.GeneralCard;
import it.polimi.ingsw.model.board.Playground;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable class representing the objective card.
 */
public abstract class ObjectiveCard implements GeneralCard {

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

    public abstract int calculatePoints(Playground p);

    @Override
    public int getFrontId() {
        return frontId;
    }

    @Override
    public int getBackId() {
        return backId;
    }

    @Override
    public Map<Symbol, Integer> getRequiredResources() {
        return new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectiveCard that = (ObjectiveCard) o;
        return multiplier == that.multiplier;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(multiplier);
    }
}
