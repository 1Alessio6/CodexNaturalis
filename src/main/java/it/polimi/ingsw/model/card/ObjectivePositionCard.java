package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;

import java.util.HashMap;
import java.util.Map;

public abstract class ObjectivePositionCard extends ObjectiveCard {
    private final Map<Position, Color> condition;

    public ObjectivePositionCard(Map<Position, Color> condition, int multiplier) throws IllegalArgumentException {
        super(multiplier);

        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.condition = condition;
    }

    public Map<Position, Color> getCondition(){
        return new HashMap<>(condition);
    }

    @Override
    public abstract int calculatePoints(Playground p);

    int updateCount(Map<Position, Tile> area, int count, Position pos0, Position pos1, Position pos2, Color col0, Color col1, Color col2){
        if(area.get(pos0).getFace().getColor() == col0 && area.get(pos1).getFace().getColor() == col1 && area.get(pos2).getFace().getColor() == col2) {
            count++;
            area.remove(pos0);
            area.remove(pos1);
            area.remove(pos2);
        }

        return count;
    }

}
