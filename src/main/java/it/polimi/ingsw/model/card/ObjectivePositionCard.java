package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.CardColor;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Corresponds to the card that gives points to the player when the player places them in accordance with the pattern.
 */
public final class ObjectivePositionCard extends ObjectiveCard {
    private final Map<Position, CardColor> condition;

    /**
     * Constructs an objective position card with no parameter provided
     */
    public ObjectivePositionCard() {
        super();
        condition = new HashMap<>();
    }

    /**
     * Constructs an objective position card with the <code>condition</code> and <code>multiplier</code> provided
     *
     * @param condition  with the positions and colors of the cards
     * @param multiplier of the objective position card
     * @throws IllegalArgumentException if the condition that has been passed is null
     */
    public ObjectivePositionCard(Map<Position, CardColor> condition, int multiplier) throws IllegalArgumentException {
        super(multiplier);

        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        this.condition = condition;
    }

    /**
     * Returns the condition of the objective position card
     *
     * @return a map with the positions and colors of the cards
     */
    public Map<Position, CardColor> getCondition(){
        return new HashMap<>(condition);
    }

    /**
     * Verifies that the <code>requiredPosition</code> and <code>requiredColor</code> provided are on the
     * <code>playground</code>
     *
     * @param playground       in which position and color are to be verified
     * @param requiredPosition to be verified
     * @param requiredColor    of the card
     * @return true if there is a card in the given position with the specified color on the playground, false otherwise
     */
    private boolean matchRequirement(Playground playground, Position requiredPosition, CardColor requiredColor) {
        CardColor color;
        try {
            color = playground.getTile(requiredPosition).getFace().getColor();
        } catch (NullPointerException e) {
            color = null; // no color: tile is empty
        }

        return playground.contains(requiredPosition)
                &&
                (color != null && color.equals(requiredColor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Position, CardColor> getPositionCondition() {
        return condition;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Checks if two objects are equal, two objective position cards in particular
     *
     * @param o the object to be compared
     * @return true if this object is equal to <code>o</code>, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectivePositionCard that = (ObjectivePositionCard) o;
        return Objects.equals(condition, that.condition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), condition);
    }
}
