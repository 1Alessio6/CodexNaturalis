package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.util.Map;
import java.util.Objects;

public class Front extends Face {
    private final int score;

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
     * @throws IllegalArgumentException
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Front front = (Front) o;
        return score == front.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), score);
    }
}
