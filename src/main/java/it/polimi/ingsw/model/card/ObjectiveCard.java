package it.polimi.ingsw.model.card;

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

}
