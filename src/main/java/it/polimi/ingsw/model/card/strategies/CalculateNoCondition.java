package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;

/**
 * Strategy used to calculate points in a card that doesn't require to fulfill any condition
 */
public class CalculateNoCondition implements CalculatePoints{
    @Override
    public int calculatePoints(Position pos, Playground playground) {
        return playground.getTile(pos).getFace().getScore();
    }
}
