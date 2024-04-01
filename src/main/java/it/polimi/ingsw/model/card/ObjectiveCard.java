package it.polimi.ingsw.model.card;

public abstract class ObjectiveCard {
    private int multiplier;

    public ObjectiveCard(int multiplier) throws IllegalArgumentException {
        if (multiplier < 0) {
            throw new IllegalArgumentException("Multiplier cannot be negative");
        }
        this.multiplier = multiplier;
    }

    public void getObjective() {
    }

    public int getMultiplier() {
        return multiplier;
    }
}
