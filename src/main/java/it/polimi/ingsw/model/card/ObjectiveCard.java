package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectiveCard that = (ObjectiveCard) o;
        return multiplier == that.multiplier;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(multiplier);
    }
}
