package it.polimi.ingsw.network.client.model.card;

import it.polimi.ingsw.model.card.Symbol;

import java.util.Map;

public class ClientGoldenCard extends ClientCard {
    public void setRequirements(Map<Symbol, Integer> requirements) {
        super.requirements = requirements;
    }
}
