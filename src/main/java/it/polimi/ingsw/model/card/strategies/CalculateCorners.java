package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;

/**
 * Strategy used for golden cards that add points based on number of corners covered after it's placement
 */
public class CalculateCorners implements CalculatePoints {
    @Override
    public int calculatePoints(Position pos, Playground playground) {
        int multiplier = playground.getTile(pos).getFace().getScore();
        int count = 0;

        /* checks for top/bottom left/right positions availabilities */
        for (int i = pos.getX() - 1; i - pos.getX() <= 2; i += 2)
            for (int j = pos.getY() - 1; j - pos.getY() <= 2; j += 2)
                count += playground.getTile(new Position(i,j)).sameAvailability(Availability.OCCUPIED) ? 1 : 0;

        return count * multiplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
