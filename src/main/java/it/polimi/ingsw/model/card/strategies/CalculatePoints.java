package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.*;

/**
 * Calculate Points is an interface capable of calculating the points earned
 * depending on the particular encountered.
 */
public interface CalculatePoints {
    public int calculatePoints (Position pos, Playground playground);
}
