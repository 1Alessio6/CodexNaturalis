package it.polimi.ingsw.model.board;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a position on a hypothetical Cartesian plane with x and y axes.
 */

public class Position implements Serializable {

    //attributes
    private final int x;
    private final int y;


    /**
     * Constructs a position at the coordinates x and y provided.
     *
     * @param x the position's abscissa.
     * @param y the position's ordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a position given an existing <code>other</code> position
     *
     * @param other the existing position provided
     */
    public Position(Position other) {
        x = other.getX();
        y = other.getY();
    }

    /**
     * Returns position's abscissa.
     *
     * @return an int value representing the abscissa of the position.
     */
    public int getX() {
        return x;
    }


    /**
     * Returns position's ordinate.
     *
     * @return an int value representing the ordinate of the position.
     */
    public int getY() {
        return y;
    }


    /**
     * Checks if two objects are equal.
     * Overrides the equals() method in the Object class.
     *
     * @return true if this object it's equal to obj, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        return this.getX() == ((Position) obj).getX() && this.getY() == ((Position) obj).getY();
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
        return this.x * this.y;
    }
    /* the override is needed in order to use position as a key of a map, and check if there's a key in the map with different
    reference, but with the same attributes */


    /**
     * Returns a string representation of Position.
     * Overrides the toString() method in the Object class.
     *
     * @return a string representing the position.
     */
    @Override
    public String toString() {
        return "{" + this.x + "," + this.y + "}";
    }

    /**
     * Sums two position's coordinates.
     *
     * @param lhs the first position whose coordinates are summed
     * @param rhs the second position whose coordinates are summed
     * @return a position that has as coordinate the result of the two parameter's coordinates.
     */
    public static Position sum(Position lhs, Position rhs) {
        return new Position(lhs.x + rhs.x, lhs.y + rhs.y);
    }

    /**
     * Subtracts two position's coordinates.
     *
     * @param lhs the first position whose coordinates are subtracted
     * @param rhs the second position whose coordinates are subtracted
     * @return a position that has as coordinates the result of the two parameter's coordinates
     */
    public static Position diff(Position lhs, Position rhs) {
        return new Position(lhs.x - rhs.x, lhs.y - rhs.y);
    }

    /**
     * Returns the positions of the corners of a card, taking the centre of the card as the origin.
     *
     * @return a list with the offset positions
     */
    public static List<Position> getOffsets() {
        return Arrays.asList(
                new Position(1, -1),
                new Position(1, 1),
                new Position(-1, 1),
                new Position(-1, -1)
        );
    }
}
