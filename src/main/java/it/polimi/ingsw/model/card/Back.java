package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculateBackPoints;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Back is a class that extends Face and represents the back of the face
 */
public class Back extends Face {

    /**
     * Map containing the resources of the back and their quantity.
     */
    private final Map<Symbol, Integer> resources;

    /**
     * Constructs a back card with no parameters provided
     */
    public Back() {
        super();
        resources = new HashMap<>();
    }

    /**
     * Constructs a back card with the color, corners and resources provided.
     *
     * @param color     of the card.
     * @param corners   of the card.
     * @param resources provided by the card.
     */
    public Back(CardColor color, Map<CornerPosition, Corner> corners, Map<Symbol, Integer> resources) throws IllegalArgumentException {
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
     * @return resources map
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
    public Map<Symbol, Integer> getBackCenterResources() {
        return resources;
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

    /**
     * Checks if two objects are equal, two backs in particular.
     *
     * @param o the object to be compared
     * @return true if this object is equal to <code>o</code>, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Back back = (Back) o;
        return Objects.equals(resources, back.resources);
    }

    /**
     * Returns a hashcode depending on the attributes.
     * Given two instance with the same attributes this method returns the same hashcode.
     * Overrides the hashCode() method in the Object class.
     *
     * @return an int value representing the hashcode of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), resources);
    }
}
