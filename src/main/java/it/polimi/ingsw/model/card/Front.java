package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.util.HashMap;
import java.util.Map;

public class Front extends Face {
    private final int score;

    /**
     * Constructs a front card with the color, score and corners provided.
     *
     * @param color   of the card.
     * @param corners of the card.
     * @param score   obtained after positioning the card.
     * @throws IllegalArgumentException if any argument is null or there are not 4 corners or the score is negative.
     */
    public Front(Color color, Map<CornerPosition, Corner> corners, int score) throws IllegalArgumentException {
        super(color, corners, new CalculateNoCondition());

        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }

        this.score = score;
    }

    public Front(Color color, Map<CornerPosition, Corner> corners, int score, CalculatePoints calculator) throws IllegalArgumentException {
        super(color, corners, calculator);

        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }

        this.score = score;
    }

    /**
     * ad hoc constructor for StartingCards
     * @param corners of startingCard
     * @throws IllegalArgumentException
     */
    public Front(Map<CornerPosition, Corner> corners) throws IllegalArgumentException {
        super(null, corners, new CalculateNoCondition());

        this.score = 0;
    }

    public Map<Symbol, Integer> getResources() {
        Map<Symbol, Integer> resources = new HashMap<>();
        for(CornerPosition c : this.getCorners().keySet()){
            Symbol s = this.getCorners().get(c).getSymbol();

            if(s != null){
                if(resources.containsKey(s)){
                    resources.put(s,resources.get(s) + 1);
                }
                else{
                    resources.put(s,1);
                }
            }
        }

        return resources;
    }

    public int getScore() {
        return score;
    }

    public int calcPoints(Playground p) {
        return score;
    }

    public String toString(){

        StringBuilder cornerString = new StringBuilder();

        for(CornerPosition c : this.getCorners().keySet()){ //todo update when deserializer is fixed
             if(this.getCorners().get(c) != null) {
                 cornerString.append(c + ": ").append(this.getCorners().get(c).toString()).append(" - ");
             }
        }
        cornerString.append("END");

        return "{ Color: " + this.getColor() + "| Points" + this.score + " ) " + "| Corners: [ " + cornerString + " ] ";
    }

}
