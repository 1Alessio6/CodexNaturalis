package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;

import it.polimi.ingsw.model.board.Playground;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObjectiveResourceCard extends ObjectiveCard {
    Map<Symbol,Integer> condition=new HashMap<>();

    public ObjectiveResourceCard(Map<Symbol,Integer> condition, int multiplier) {
        super(multiplier);
        this.condition = condition;
    }

    @Override
    public int calculatePoints(Playground playground) {

        if(condition.size()==1) { // one type of symbol case

            int c = 0;
            // take the unique symbol condition present in condition HashMap
            Object object = condition.keySet().toArray()[0];

            if (playground.getResources().containsKey(object)) { //verify whether the symbol is contained or not in resources HashMap
                //calculate the number of times that the condition is fulfilled and return the points
                c = (int) Math.floor((double) playground.getResources().get(object) / condition.get(object));
                return c * getMultiplier();

            } else { //unfulfilled condition
                return c;
            }
        }
        else { //more than one type of symbol case

            AtomicInteger counter=new AtomicInteger();
            // create a copy of condition in temporalMap
            HashMap<Symbol,Integer> temporalMap=new HashMap<>(condition);

            temporalMap.forEach((k,v)->{ // for each k in temporalMap, temporalMap values are updated to the values contained in resources map
                condition.put(k,playground.getResources().get(k));
            });

            temporalMap.forEach((k,v)->{ //update temporalMap values every time the condition is fulfilled
                while(temporalMap.get(k)-condition.get(k)>0){
                    condition.put(k,temporalMap.get(k)-condition.get(k));
                    counter.getAndIncrement();
                }
            });

            //return points
            return counter.intValue()*getMultiplier();

        }
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


