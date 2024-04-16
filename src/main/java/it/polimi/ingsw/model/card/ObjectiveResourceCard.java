package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;

import it.polimi.ingsw.model.board.Playground;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObjectiveResourceCard extends ObjectiveCard {
    private final Map<Symbol,Integer> condition;

    public ObjectiveResourceCard(Map<Symbol,Integer> condition, int multiplier) {
        super(multiplier);
        this.condition = condition;
    }

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

}


