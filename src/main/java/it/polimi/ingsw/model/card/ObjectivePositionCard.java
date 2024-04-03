package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Position;

import java.util.Map;

public final class ObjectivePositionCard extends ObjectiveCard {
    private final Map<Position, Color> condition;

    public ObjectivePositionCard(Map<Position, Color> condition, int multiplier) throws IllegalArgumentException {
        super(multiplier);

        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.condition = condition;
    }
}
