package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.util.List;
import java.util.Map;

public final class GoldenFront extends Front {
   private Condition pointsCondition; /* should be enum? */
   private List<Symbol> requirements; /* set? */

    /**
     * Constructs a front card with the color, score and corners provided.
     *
     * @param color   of the card.
     * @param corners of the card.
     * @param score   obtained after positioning the card.
     * @throws IllegalArgumentException if any argument is null or there are not 4 corners or the score is negative.
     */
    public GoldenFront(Color color, Map<CornerPosition, Corner> corners, int score, CalculatePoints calculator) throws IllegalArgumentException {
            super(color, corners, score, calculator);
    }

    public List<Symbol> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Symbol> requirements) {
        this.requirements = requirements;
    }

    public Condition getPointsCondition() {
        return pointsCondition;
    }

    public void setPointsCondition(Condition pointsCondition) {
        this.pointsCondition = pointsCondition;
    }
}
