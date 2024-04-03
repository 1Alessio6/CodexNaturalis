package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;

public abstract class ObjectiveCard {
    int multiplier;

    public ObjectiveCard(int multiplier) {
        this.multiplier = multiplier;
    }

    public void getObjective() {
    }

    public int getMultiplier() {
        return multiplier;
    }

    public abstract int calculatePoints(Playground p);

}
