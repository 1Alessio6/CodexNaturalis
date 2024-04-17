package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;

/**
 * Strategy used to calculate the points for all type of cards in its back side
 */
public class CalculateBackPoints implements CalculatePoints{
    @Override
    public int calculatePoints(Position pos, Playground playground) {

        return 0;

    }
}
