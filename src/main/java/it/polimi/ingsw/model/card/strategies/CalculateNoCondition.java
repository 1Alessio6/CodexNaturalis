package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;

public class CalculateNoCondition implements CalculatePoints{
    @Override
    public int calculatePoints(Position pos, Playground playground) {
        return playground.getTile(pos).getFace().getScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
