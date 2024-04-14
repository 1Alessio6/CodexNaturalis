package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;

public class CalculateNoCondition implements CalculatePoints{
    @Override
    public int calculatePoints(Position pos, Playground playground) {
        return playground.getTile(pos).getFace().getScore();
    }
}
