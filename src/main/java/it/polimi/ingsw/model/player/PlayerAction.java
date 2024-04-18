package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A class containing all methods representing possible player's action.
 * The fsm is
 * ChooseStarter -> ChooseObjective -> Place -> Draw -> Place
 * NOTE. Each Player Action is independent of the game's state, it only models which operation the player can do provided
 * the right context.
 */

// state design pattern: PlayerAction is the current player's state.
public abstract class PlayerAction {


    /**
     * Allows a player to place a card on the side and position specified.
     *
     * @param player   who performs the placement.
     * @param card     to place.
     * @param side     of the card.
     * @param position in the playground.
     * @return TODO
     * @throws InvalidPlayerActionException            if the player cannot perform the operation.
     * @throws Playground.UnavailablePositionException if the position is not available.
     * @throws Playground.NotEnoughResourcesException  if the player's resources are not enough to place the card.
     */
    int placeCard(Player player, Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        throw new InvalidPlayerActionException("Player cannot place a card");
    }


    /**
     * Allows the player to place the secret objective card.
     *
     * @param player          who performs the placement.
     * @param chosenObjective card.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    void placeObjectiveCard(Player player, int chosenObjective) throws InvalidPlayerActionException {
        throw new InvalidPlayerActionException("Player has already choose an objective");
    }


    /**
     * Adds a new card to the player's card list.
     *
     * @param player  to which the card is added.
     * @param newCard to be added.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    void addCard(Player player, Card newCard) throws InvalidPlayerActionException {
        throw new InvalidPlayerActionException("Player cannot draw");
    }

    //void drawFromDeck(Player player, Deck<Card> deck) throws InvalidPlayerActionException, EmptyDeckException {
    //    throw new InvalidPlayerActionException("Player cannot draw");
    //}

    //void drawFromFaceUpCards(Player player, List<Card> faceUpCards, int cardIdx, Deck<Card> deck) throws InvalidPlayerActionException, EmptyDeckException {
    //    throw new InvalidPlayerActionException("Player cannot draw");
    //}


    /**
     * Updates the state of the Player Action.
     *
     * @param player     to whom the update is applied.
     * @param nextAction of the Player Action.
     */
    void nextAction(Player player, PlayerAction nextAction) {
        player.setAction(nextAction);
    }


    /**
     * Returns true if the set-up has finished.
     *
     * @return false: by default the set-up is not fully established until the player has chosen the secret objective,
     * in the ChooseObjective PlayerAction.
     */
    boolean isSetupFinished() {
        return false;
    }

}


/**
 * This class is responsible for choosing the starter card side and its placement.
 */
class ChooseStarter extends PlayerAction {

    /**
     * {@inheritDoc}
     */
    @Override
    int placeCard(Player player, Card card, Side side, Position position)
            throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        player.getPlayground().placeCard(card.getFace(side), position);
        nextAction(player, new ChooseObjective());
        // starter doesn't increment player's points
        assert (player.getPoints() == 0);
        return player.getPoints();
    }
}


/**
 * This class is responsible for managing the choice of the secret objective card.
 */
class ChooseObjective extends PlayerAction {

    /**
     * {@inheritDoc}
     */
    @Override
    void placeObjectiveCard(Player player, int chosenObjective) throws InvalidPlayerActionException {
        List<ObjectiveCard> objectiveCards = player.getObjectives();
        assert 0 <= chosenObjective && chosenObjective < objectiveCards.size();

        ObjectiveCard chosenObjectiveCard = objectiveCards.get(chosenObjective);
        objectiveCards.clear();
        // now only one objective card (the chosen objective) will be available.
        objectiveCards.add(chosenObjectiveCard);

        nextAction(player, new Place());
    }
}


/**
 * This class is responsible for the placement of all the cards from the moment in which the secret objective card is
 * chosen until the moment in which the last card is placed at the end of the game.
 */
class Place extends PlayerAction {

    /**
     * @inheritDoc}
     */
    @Override
    int placeCard(Player player, Card card, Side side, Position position)
            throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        assert (player.has(card));
        player.getPlayground().placeCard(card.getFace(side), position);
        player.discard(card);

        nextAction(player, new Draw());
        return player.getPoints();
    }


    /**
     * Returns true if the set-up has finished.
     *
     * @return true: set-up has finished in place PlayerAction's state.
     */
    @Override
    boolean isSetupFinished() {
        return true;
    }
}


/**
 * This class is responsible for drawing all kind of cards from the moment in which the secret objective card is
 * chosen until the moment in which the last card is drawn before the end of the game.
 */
class Draw extends PlayerAction {


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
    void addCard(Player player, Card newCard) throws InvalidPlayerActionException {
        List<Card> cards = player.getCards();
        assert (cards.size() == 2);
        cards.add(newCard);

        nextAction(player, new Place());
    }


    /**
     * Returns true if the set-up has finished.
     *
     * @return true: set-up has finished in draw PlayerAction's state.
     */
    @Override
    boolean isSetupFinished() {
        return true;
    }
}
