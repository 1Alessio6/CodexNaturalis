package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;

import java.util.Map;

public class StrategyCorners implements CalculatePoints {


    //it's possible to change with an O(1) time complexity
    @Override
    public int calculatePoints(Position pos, Playground playground) {
        int multiplier = playground.getTile(pos).getFace().getScore();

        return Math.toIntExact(
                playground.getAllPositions()
                .stream()
                .filter(p -> (Math.abs(p.getX() - pos.getX()) <= 1)
                        && (Math.abs(p.getY() - pos.getY()) <= 1)
                        && !p.equals(pos)
                        &&
                        playground.getTile(pos).sameAvailability(Availability.OCCUPIED))
                .count()) * multiplier;
    }
}
