package it.polimi.ingsw.network.client.model.card;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.color.CardColor;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.Symbol;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents the objective card in the client's side.
 */
public class ClientObjectiveCard implements Serializable {

    private final int frontID;
    private final int backID;
    private final Map<Position, CardColor> positionCondition;

    private final Map<Symbol,Integer> resourceCondition;
    private int score;


    /**
     * Constructs an objective card with the <code>objectiveCard</code> provided.
     *
     * @param objectiveCard from which the client objective card is to be constructed.
     */
    public ClientObjectiveCard(ObjectiveCard objectiveCard) {
        frontID = objectiveCard.getFrontId();
        backID = objectiveCard.getBackId();
        positionCondition = objectiveCard.getPositionCondition();
        resourceCondition = objectiveCard.getResourceCondition();
        score=objectiveCard.getMultiplier();
    }

    public int getBackID() {
        return backID;
    }

    public int getFrontID() {
        return frontID;
    }

    public Map<Symbol, Integer> getResourceCondition() {
        return resourceCondition;
    }

    public Map<Position, CardColor> getPositionCondition() {
        return positionCondition;
    }

    public String getPath(){
        return "gui/png/objective_cards/" + frontID + ".png";
    }

    public int getScore(){return score;}
}
