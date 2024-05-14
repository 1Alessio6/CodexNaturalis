package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.CardColor;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ObjectivePositionCard extends ObjectiveCard {
    private final Map<Position, CardColor> condition;

    public ObjectivePositionCard() {
        super();
        condition = new HashMap<>();
    }

    public ObjectivePositionCard(Map<Position, CardColor> condition, int multiplier) throws IllegalArgumentException {
        super(multiplier);

        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.condition = condition;
    }

    public Map<Position, CardColor> getCondition(){
        return new HashMap<>(condition);
    }

    private boolean matchRequirement(Playground playground, Position requiredPosition, CardColor requiredColor) {
        return playground.contains(requiredPosition)
                && playground.getTile(requiredPosition).getFace().getColor().equals(requiredColor);
    }

    @Override
    public Map<Position, CardColor> getPositionCondition() {
        return condition;
    }

    @Override
    public int calculatePoints(Playground playground) {
        int count = 0;

        Set<Position> alreadyVisitedPositions = new HashSet<>();

        for(Position p : playground.getAllPositions()){

            boolean isSatisfied = true;

            List<Position> usedPositions = new ArrayList<>();

            for (Map.Entry<Position, CardColor> entry : condition.entrySet()) {

                Position requiredPosition = Position.sum(p, entry.getKey());
                CardColor requiredColor = entry.getValue();

                if (alreadyVisitedPositions.contains(requiredPosition)
                        || !matchRequirement(playground, requiredPosition, requiredColor))
                {
                    isSatisfied = false;
                    break;
                } else {
                    usedPositions.add(requiredPosition);
                }
            }

            if (isSatisfied) {
                count += 1;
                alreadyVisitedPositions.addAll(usedPositions);
            }

        }

        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectivePositionCard that = (ObjectivePositionCard) o;
        return Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), condition);
    }
}
