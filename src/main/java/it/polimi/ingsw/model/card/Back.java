package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.strategies.CalculateBackPoints;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Back extends Face {

    /**
     * Map containing the resources of the back and their quantity.
     */
    private final Map<Symbol, Integer> resources;

    /**
     * Constructs a front card with the color, corners and resources provided.
     *
     * @param color     of the card.
     * @param corners   of the card.
     * @param resources provided by the card.
     */
    public Back(Color color, Map<CornerPosition, Corner> corners, Map<Symbol, Integer> resources) throws IllegalArgumentException {
        super(color, corners, new CalculateBackPoints());

        if (resources == null) {
            throw new IllegalArgumentException("Resources cannot be null");
        }

        for (Integer num : resources.values()) {
            if (num <= 0) {
                throw new IllegalArgumentException("Symbol occurrences cannot be negative or zero, otherwise it wouldn't be a resource");
            }
        }

        this.resources = resources;
    }

    /**
     * Returns a map with the resources present in the back.
     *
     * @return resoucers map
     */
    public Map<Symbol, Integer> getResources() {

        Map<Symbol, Integer> totalResources = super.getResources(); //totalResources is only initialized with corner's resource

        for (Symbol s : resources.keySet()) {            //sum corner's resource with back's resources
            if (totalResources.containsKey(s)) {         //update the value
                totalResources.put(s, totalResources.get(s) + resources.get(s));
            } else {
                totalResources.put(s, resources.get(s));
            }
        }

        return totalResources;
    }

    /**
     * Returns the score present in the back
     *
     * @return backs score
     */
    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public Condition getCondition() {
        return null;
    }

    /**
     * Facilitates the deserialization of the different cards present in the json.
     *
     * @return modified string
     */
    public String toString() {

        StringBuilder symbolString = new StringBuilder();
        StringBuilder cornerString = new StringBuilder();

        for (Symbol s : Symbol.values()) {
            if (resources.containsKey(s)) {
                symbolString.append(s).append(" - ");
            }
        }
        symbolString.append("END");

        for (CornerPosition c : this.getCorners().keySet()) {
            cornerString.append(c + ": ").append(this.getCorners().get(c).toString()).append(" - ");
        }
        cornerString.append("END");

        return "{ Color: " + this.getColor() + "| Symbol: ( " + symbolString + " ) " + "| Corners: [ " + cornerString + " ] ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Back back = (Back) o;
        return Objects.equals(resources, back.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), resources);
    }
}
