package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Symbol;

import java.util.Map;

public interface GeneralCard {
    int getFrontId();

    int getBackId();

    Map<Symbol, Integer> getRequiredResources();

}
