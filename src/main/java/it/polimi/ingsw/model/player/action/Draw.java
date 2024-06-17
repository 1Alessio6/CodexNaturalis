package it.polimi.ingsw.model.player.action;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

/**
 * This class is responsible for drawing all kind of cards from the moment in which the secret objective card is
 * chosen until the moment in which the last card is drawn before the end of the game.
 */
public class Draw extends PlayerAction {


    /**
     * Private method that consents to add a new card to the player's card list.
     *
     * @param player to which the card is added.
     * @param card   tobe added.
     */
    private void addNewCard(Player player, Card card) {
        List<Card> cards = player.getCards();
        assert (cards.size() == 2);
        cards.add(card);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addCard(Player player, Card newCard) {
        List<Card> cards = player.getCards();
        assert (cards.size() == 2);
        cards.add(newCard);

        nextAction(player, new Place());
    }

    @Override
    public PlayerState getPlayerState() {
        return PlayerState.Draw;
    }
}
