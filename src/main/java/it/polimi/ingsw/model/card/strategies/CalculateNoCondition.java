package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;

/**
 * Strategy used to calculate points in a card that doesn't require to fulfill any condition
 */
public class CalculateNoCondition implements CalculatePoints{
    /**
     * Calculates the points in a card when it doesn't have points condition
     *
     * @param pos        the position in the playground.
     * @param playground the playground on which the calculation is to be carried.
     * @return the calculated points.
     */
    @Override
    public int calculatePoints(Position pos, Playground playground) {
        return playground.getTile(pos).getFace().getScore();
    }

    /**
     * Checks if two objects are equal.
     *
     * @param o the object to be compared
     * @return true if this object is equal to <code>o</code>, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
