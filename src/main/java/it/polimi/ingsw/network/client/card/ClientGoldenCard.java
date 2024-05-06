package it.polimi.ingsw.network.client.card;

import it.polimi.ingsw.model.card.Symbol;

import java.util.HashMap;
import java.util.Map;

public class ClientGoldenCard extends ClientCard {
    private Map<Symbol, Integer> requirements;

    public ClientGoldenCard() {
        super();
        requirements = null;
    }

    public ClientGoldenCard(int frontId, int backId, Map<Symbol, Integer> requirements) {
        super(frontId, backId);
        this.requirements = requirements;
    }

    public Map<Symbol, Integer> getRequiredResources() {
        return requirements;
    }
}
