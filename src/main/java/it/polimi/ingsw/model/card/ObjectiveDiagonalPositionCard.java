package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;

import java.util.Map;

public class ObjectiveDiagonalPositionCard extends ObjectivePositionCard{
    public ObjectiveDiagonalPositionCard(Map<Position, Color> condition, int multiplier) throws IllegalArgumentException {
        super(condition, multiplier);
    }

    @Override
    public int calculatePoints(Playground p) {
        int count = 0;
        Map<Position, Tile> area = p.getArea(); //copy of the area in the playground

        for(Position i : area.keySet()){
            int x = i.getX();
            int y = i.getY();

            Position pos1 = new Position(x + 1, y + 1);
            Position pos2 = new Position(x + 2, y + 2);
            Color col0 = this.getCondition().get(new Position(0,0));
            Color col1 = this.getCondition().get(new Position(1,1));
            Color col2 = this.getCondition().get(new Position(2,2));

            if(area.containsKey(pos1) && area.containsKey(pos2)) {
                if(area.containsKey(pos1) && area.containsKey(pos2)){
                    count = this.updateCount(area, count, i, pos1, pos2, col0, col1, col2);
                }
            }
        }

        return count;
    }
}
