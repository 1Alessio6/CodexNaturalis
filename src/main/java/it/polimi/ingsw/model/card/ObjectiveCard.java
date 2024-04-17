package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;

/**
 * Immutable class representing the objective card.
 */

public abstract class ObjectiveCard {

    /**
     * Represents the number of points won when card is placed.
     */
    int multiplier;

    /**
     * Constructs an objective card with given provided.
     *
     * @param multiplier of the objective card
     */
    public ObjectiveCard(int multiplier) {
        this.multiplier = multiplier;
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

}
