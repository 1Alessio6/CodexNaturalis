package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.strategies.CalculateCorners;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.card.strategies.CalculateResources;

import java.util.Map;

public final class GoldenFront extends Front {
   private final Condition pointsCondition; /* should be enum? */
   private final Map<Symbol, Integer> requirements; /* set? */

    /**
     * Constructs a front card with the color, score and corners provided.
     *
     * @param color   of the card.
     * @param corners of the card.
     * @param score   obtained after positioning the card.
     * @throws IllegalArgumentException if any argument is null or there are not 4 corners or the score is negative.
     */
    public GoldenFront(Color color, Map<CornerPosition, Corner> corners, int score, Condition pointsCondition, Map<Symbol, Integer> requirements) throws IllegalArgumentException {
        CalculatePoints calculator;
        switch (pointsCondition){
            case CORNERS:
                calculator = new CalculateCorners();
                break;
            case NUM_MANUSCRIPT:
            case NUM_INKWKELL:
            case NUM_QUILL:
                calculator = new CalculateResources();
                break;
            case null:
                calculator = new CalculateNoCondition();
                break;
        }

        super(color, corners, score, calculator);
        this.pointsCondition = pointsCondition;
        this.requirements = requirements;
    }

    public Map<Symbol, Integer> getRequirements() {
        return requirements;
    }

    public Condition getPointsCondition() {
        return pointsCondition;
    }
}
