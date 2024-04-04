package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;

import java.util.Map;

public class ObjectiveLPositionCard extends ObjectivePositionCard{
    public ObjectiveLPositionCard(Map<Position, Color> condition, int multiplier) throws IllegalArgumentException {
        super(condition, multiplier);
    }

    @Override
    public int calculatePoints(Playground p) {

        int count = 0;
        Map<Position, Tile> area = p.getArea(); //copy of the area in the playground

        for(Position i : area.keySet()){
            int x = i.getX();
            int y = i.getY();

            Position pos1 = new Position(x, y + 2);
            Position pos2 = new Position(x + 1, y + 3);
            Color col0 = this.getCondition().get(new Position(0,0));
            Color col1 = this.getCondition().get(new Position(0,2));
            Color col2 = this.getCondition().get(new Position(1,3));

            if(area.containsKey(pos1) && area.containsKey(pos2)){
                if(area.get(i).getFace().getColor() == col0 && area.get(pos1).getFace().getColor() == col1 && area.get(pos2).getFace().getColor() == col2) {
                    count++;
                    area.remove(i);
                    area.remove(pos1);
                    area.remove(pos2);
                }
            }
        }

        return count;

    }
}

