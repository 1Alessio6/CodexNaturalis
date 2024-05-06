package it.polimi.ingsw.network.client.card;

import it.polimi.ingsw.model.card.Symbol;

import java.util.HashMap;
import java.util.Map;

public class ClientCard {
    int frontId;
    int backId;

    public ClientCard() {
        frontId = -1;
        backId = -1;
    }

    public ClientCard(int frontId, int backId) {
        this.frontId = frontId;
        this.backId = backId;
    }

    public int getBackId() {
        return backId;
    }

    public int getFrontId() {
        return frontId;
    }

    public Map<Symbol, Integer> getRequiredResources() {
        return new HashMap<>();
    }

}
