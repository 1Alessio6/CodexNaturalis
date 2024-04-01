package it.polimi.ingsw.model.card;

import java.util.List;
import java.util.Set;

public final class ObjectiveResourceCard extends ObjectiveCard {
    List<Symbol> condition;

    public ObjectiveResourceCard(List<Symbol> condition, int multiplier) throws  IllegalArgumentException {
        super(multiplier);

        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be nul");
        }

        this.condition = condition;
    }
}
