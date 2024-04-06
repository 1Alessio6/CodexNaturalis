package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;

import java.util.*;

public abstract class ObjectivePositionCard extends ObjectiveCard {
    private final Map<Position, Color> condition;

    public ObjectivePositionCard(Map<Position, Color> condition, int multiplier) throws IllegalArgumentException {
        super(multiplier);

        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.condition = condition;
    }

    public Map<Position, Color> getCondition(){
        return new HashMap<>(condition);
    }

    private boolean matchRequirement(Playground playground, Position requiredPosition, Color requiredColor) {
        return playground.contains(requiredPosition)
                && playground.getTile(requiredPosition).getFace().getColor().equals(requiredColor);
    }

    @Override
    public int calculatePoints(Playground playground) {
        int count = 0;

        Set<Position> alreadyVisitedPositions = new HashSet<>();

        for(Position p : playground.getAllPositions()){

            boolean isSatisfied = true;

            List<Position> usedPositions = new ArrayList<>();

            for (Map.Entry<Position, Color> entry : condition.entrySet()) {

                Position requiredPosition = Position.sum(p, entry.getKey());
                Color requiredColor = entry.getValue();

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

}
