package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;

/**
 * Strategy used to calculate the points for all type of cards in its back side
 */
public class CalculateBackPoints implements CalculatePoints {

    /**
     * Calculate the points found on the front of the card.
     *
     * @param pos        in the playground.
     * @param playground on which the calculation will be performed.
     * @return 0: by default cards doesn't have points in its back.
     */
    @Override
    public int calculatePoints(Position pos, Playground playground) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
