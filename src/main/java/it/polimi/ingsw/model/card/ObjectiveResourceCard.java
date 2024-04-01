package it.polimi.ingsw.model.card;

import java.util.List;
import java.util.Set;

public final class ObjectiveResourceCard extends ObjectiveCard {
    List<Symbol> condition;

    public ObjectiveResourceCard(List<Symbol> condition, int multiplier) {
        this.condition = condition;
        this.multiplier = multiplier;
    }
}
