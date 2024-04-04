package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;

import java.util.Map;

public class StrategyCorners implements CalculatePoints {

    @Override
    public int calculatePoints(Position pos, Playground playground) {
        int multiplier = playground.getArea().get(pos).getFace().getScore();
        Map<Position, Tile> area = playground.getArea();

        return Math.toIntExact(
                area
                .keySet()
                .stream()
                .filter(p -> (Math.abs(p.getX() - pos.getX()) <= 1)
                        && (Math.abs(p.getY() - pos.getY()) <= 1)
                        && !p.equals(pos)
                        && area.get(p).getAvailability()
                        .equals(Availability.OCCUPIED))
                .count()) * multiplier;
    }
}
