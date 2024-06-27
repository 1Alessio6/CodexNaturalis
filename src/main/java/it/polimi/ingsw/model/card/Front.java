package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.card.color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.util.Map;
import java.util.Objects;

/**
 * Front is a class that extends Face and represents the front of the face
 */
public class Front extends Face {
    private final int score;

    /**
     * Constructs a front card with no parameters provided.
     */
    public Front() {
        super();
        score = 0;
    }

    /**
     * Constructs a front card with the color, score and corners provided.
     *
     * @param color   of the card.
     * @param corners of the card.
     * @param score   obtained after positioning the card.
     * @throws IllegalArgumentException if any argument is null or there are not 4 corners or the score is negative.
     */
    public Front(CardColor color, Map<CornerPosition, Corner> corners, int score) throws IllegalArgumentException {
        super(color, corners, new CalculateNoCondition());

        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }

        this.score = score;
    }

    /**
     * Constructs a front card with the color, corners, score and calculator provided.
     *
     * @param color      the color of the card
     * @param corners    the corners of the card
     * @param score      the score obtained after positioning the card
     * @param calculator the calculator of the card
     * @throws IllegalArgumentException if any argument is null, there aren't 4 corners or the score is negative
     */
    public Front(CardColor color, Map<CornerPosition, Corner> corners, int score, CalculatePoints calculator) throws IllegalArgumentException {
        super(color, corners, calculator);

        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }

        this.score = score;
    }

    /**
     * ad hoc constructor for StartingCards
     *
     * @param corners of startingCard
     * @throws IllegalArgumentException if <code>cornerList</code> doesn't have 4 corners, one of them is null or the
     *                                  calculator is null.
     */
    public Front(Map<CornerPosition, Corner> corners) throws IllegalArgumentException {
        super(null, corners, new CalculateNoCondition());

        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    @Override
    public Condition getCondition() {
        return null;
    }

    public int calcPoints(Playground p) {
        return score;
    }

    /**
     * Facilitates the deserialization of the different cards present in the json.
     *
     * @return modified string
     */
    public String toString() {

        StringBuilder cornerString = new StringBuilder();

        for(CornerPosition c : this.getCorners().keySet()){
             if(this.getCorners().get(c) != null) {
                 cornerString.append(c + ": ").append(this.getCorners().get(c).toString()).append(" - ");
             }
        }
        cornerString.append("END");

        return "{ Color: " + this.getColor() + "| Points" + this.score + " ) " + "| Corners: [ " + cornerString + " ] ";
    }

    /**
     * Checks if two objects are equal, two fronts in particular.
     *
     * @param o the object to be compared
     * @return true if this object is equal to <code>o</code>, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Front front = (Front) o;
        return score == front.score;
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
        return Objects.hash(super.hashCode(), score);
    }
}
