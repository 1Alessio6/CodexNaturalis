package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.*;

/**
 * Calculate Points is an interface capable of calculating the points earned
 * depending on the particular encountered.
 */
public interface CalculatePoints {
    /**
     * Calculates the points earned after the placement of a card.
     *
     * @param pos        the position in the playground.
     * @param playground the playground on which the calculation is to be carried.
     * @return the calculated points.
     */
    public int calculatePoints (Position pos, Playground playground);
}
