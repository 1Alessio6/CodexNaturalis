package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;

import it.polimi.ingsw.model.board.Playground;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObjectiveResourceCard extends ObjectiveCard {
    private final Map<Symbol,Integer> condition;

    public ObjectiveResourceCard() {
        super();
        condition = new Hashtable<>();
    }

    /**
     * Constructs an objective resource card with the <code>condition</code> and <code>multiplier</code> provided
     *
     * @param condition  with the resources to be had
     * @param multiplier of the objective resource card
     */
    public ObjectiveResourceCard(Map<Symbol,Integer> condition, int multiplier) {
        super(multiplier);
        this.condition = condition;
    }

    /**
     * {@inheritDoc}
     */
    public int calculatePoints(Playground playground){
        assert !condition.isEmpty();

        int min = Integer.MAX_VALUE;
        for(Symbol s : condition.keySet()) {
            if(playground.getResources().containsKey(s)){
                min = Math.min(min , playground.getResources().get(s) / condition.get(s));
            }
            else{
                return 0;
            }
        }
        return multiplier * min;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Symbol, Integer> getResourceCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectiveResourceCard that = (ObjectiveResourceCard) o;
        return Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), condition);
    }
}


