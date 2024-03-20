package it.polimi.ingsw.model.card;

import java.util.List;

public class GoldenFront extends Front {
   private String condition; /* should be enum? */
   private int multiplier; /* can be retrieved from condition */
   private List<Symbol> requirements; /* set? */

    public List<Symbol> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Symbol> requirements) {
        this.requirements = requirements;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
