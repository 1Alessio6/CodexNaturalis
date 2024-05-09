package it.polimi.ingsw.model.player.action;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.player.Player;

/**
 * This class is responsible for choosing the starter card side and its placement.
 */
public class ChooseStarter extends PlayerAction {

    /**
     * {@inheritDoc}
     */
    @Override
    public int placeCard(Player player, Card card, Side side, Position position)
            throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        player.getPlayground().placeCard(card.getFace(side), position);
        // starter doesn't increment player's points
        assert (player.getPoints() == 0);

        nextAction(player, new ChooseColor());
        return player.getPoints();
    }

    @Override
    public PlayerState getPlayerState() {
        return PlayerState.ChooseStarter;
    }
}
