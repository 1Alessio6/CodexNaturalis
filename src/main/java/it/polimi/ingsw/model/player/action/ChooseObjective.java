package it.polimi.ingsw.model.player.action;

import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

/**
 * This class is responsible for managing the choice of the secret objective card.
 */
public class ChooseObjective extends PlayerAction {

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeObjectiveCard(Player player, int chosenObjective) throws InvalidPlayerActionException {
        List<ObjectiveCard> objectiveCards = player.getObjectives();
        assert 0 <= chosenObjective && chosenObjective < objectiveCards.size();

        ObjectiveCard chosenObjectiveCard = objectiveCards.get(chosenObjective);
        objectiveCards.clear();
        // now only one objective card (the chosen objective) will be available.
        objectiveCards.add(chosenObjectiveCard);

        nextAction(player, new Place());
    }

    @Override
    public PlayerState getPlayerState() {
        return PlayerState.ChooseObjective;
    }
}
