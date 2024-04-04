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

}
