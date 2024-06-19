package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Symbol;

/**
 * Strategy used for golden cards that add points based on number of corners covered after it's placement
 */
public class CalculateCorners implements CalculatePoints {

    /**
     * Calculates the points in the golden cards with corners as condition.
     *
     * @param pos        the position in the playground.
     * @param playground the playground on which the calculation is to be carried.
     * @return the calculated points.
     */
    @Override
    public int calculatePoints(Position pos, Playground playground) {
        int multiplier = playground.getTile(pos).getFace().getScore();
        int count = playground.getAdjacentOccupiedPositions(pos).size();
        return count * multiplier;
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
