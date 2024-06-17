package it.polimi.ingsw.model.card;

import java.io.Serializable;
import java.util.Objects;

/**
 * Corner represents the corner of a card
 */
public class Corner implements Serializable {

    /**
     * Specifies whether the corner is covered or not.
     */
    private boolean isCovered;

    /**
     * Specifies the symbol that is present in the corner.
     */
    private final Symbol symbol;

    /**
     * Constructs a corner with no parameters provided.
     */
    public Corner() {
        this.isCovered = false;
        this.symbol = null;
    }


    /**
     * Constructs a corner with the given symbol.
     *
     * @param symbol of the corner.
     */
    public Corner(Symbol symbol) {
        this.isCovered = false;
        this.symbol = symbol;
    }

    /**
     * Returns the symbol present in a corner.
     *
     * @return the symbol in the corner.
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * Returns true if the corner is covered, false otherwise
     *
     * @return a boolean depending on the situation of the corner
     */
    public boolean isCovered() {
        return isCovered;
    }

    /**
     * Change the state of the corner to 'covered'
     */
    public void setCovered() {
        this.isCovered = true;
    }


    /**
     * Facilitates the deserialization of the cards.
     *
     * @return "covered" if the corner is covered,"empty corner" if the corner is empty and the symbol that is present
     * in the corner otherwise.
     */
    @Override
    public String toString() {
        if (this.isCovered) {
            return " Covered ";
        }
        if (symbol == null) {
            return " EmptyCorner";
        }
        return String.valueOf(symbol);
    }

    /**
     * Checks if two objects are equal, two corners in particular.
     *
     * @param o the object to be compared
     * @return true if this object is equal to <code>o</code>, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corner corner = (Corner) o;
        return isCovered == corner.isCovered && symbol == corner.symbol;
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
        return Objects.hash(isCovered, symbol);
    }
}
