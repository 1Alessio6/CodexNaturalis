package it.polimi.ingsw.model.card;

import java.util.List;
import java.util.Map;

public class Back extends Face {
    private Map<Symbol, Integer> resources;

    /**
     * Constructs a front card with the color, corners and resources provided.
     * @param color of the card.
     * @param corners of the card.
     * @param resources provided by the card.
     */
    public Back (Color color, Map<CornerPosition, Corner> corners, Map<Symbol,Integer> resources) throws IllegalArgumentException {
        super(color, corners);

        if (resources == null) {
            throw new IllegalArgumentException("Resources cannot be null");
        }

        for (Integer num  : resources.values()) {
            if (num <= 0) {
                throw new IllegalArgumentException("Symbol occurrences cannot be negative or zero, otherwise it wouldn't be a resource");
            }
        }

        this.resources = resources;
    }

    public Map<Symbol, Integer> getResources() {
        return resources;
    }


}
