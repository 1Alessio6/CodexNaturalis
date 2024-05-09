package it.polimi.ingsw.model.player.action;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.player.Player;

/**
 * This class is responsible for the placement of all the cards from the moment in which the secret objective card is
 * chosen until the moment in which the last card is placed at the end of the game.
 */
public class Place extends PlayerAction {

    /**
     * @inheritDoc}
     */
    @Override
    public int placeCard(Player player, Card card, Side side, Position position)
            throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        assert (player.has(card));
        player.getPlayground().placeCard(card.getFace(side), position);
        player.discard(card);

        nextAction(player, new Draw());
        return player.getPoints();
    }

    @Override
    public PlayerState getPlayerState() {
        return null;
    }
}


