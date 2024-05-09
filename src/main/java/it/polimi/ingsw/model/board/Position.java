package it.polimi.ingsw.model.board;

/**
 * This class represents a position on a hypothetical Cartesian plane with x and y axes.
 */

public class Position {

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


    //todo check if getClass in equals method should be remove
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
}
