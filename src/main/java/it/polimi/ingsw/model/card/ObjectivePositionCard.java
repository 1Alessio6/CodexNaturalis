package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Position;

import java.util.Map;

public final class ObjectivePositionCard extends ObjectiveCard {
    Map<Position, Color> condition;

    public ObjectivePositionCard(Map<Position, Color> condition, int multiplier) {
        this.condition = condition;
        this.multiplier = multiplier;
    }
}
