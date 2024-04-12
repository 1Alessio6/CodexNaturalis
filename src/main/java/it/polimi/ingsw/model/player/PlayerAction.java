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

    int placeCard(Player player, Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        throw new InvalidPlayerActionException("Player cannot place a card");
    }

    void placeObjectiveCard(Player player, ObjectiveCard objectiveCard) throws InvalidPlayerActionException {
        throw new InvalidPlayerActionException("Player has already choose an objective");
    }

    void addCard(Player player, Card newCard) throws InvalidPlayerActionException {
        throw new InvalidPlayerActionException("Player cannot draw");
    }

    //void drawFromDeck(Player player, Deck<Card> deck) throws InvalidPlayerActionException, EmptyDeckException {
    //    throw new InvalidPlayerActionException("Player cannot draw");
    //}

    //void drawFromFaceUpCards(Player player, List<Card> faceUpCards, int cardIdx, Deck<Card> deck) throws InvalidPlayerActionException, EmptyDeckException {
    //    throw new InvalidPlayerActionException("Player cannot draw");
    //}

    void nextAction(Player player, PlayerAction nextAction) {
        player.setAction(nextAction);
    }

    boolean isSetupFinished() {
        return false;
    }

}

class ChooseStarter extends PlayerAction {
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

class ChooseObjective extends PlayerAction {
    @Override
    void placeObjectiveCard(Player player, ObjectiveCard objectiveCard) throws InvalidPlayerActionException {
        List<ObjectiveCard> objectiveCards = player.getObjectives();
        assert (objectiveCards.contains(objectiveCard));
        objectiveCards.clear();
        objectiveCards.add(objectiveCard);

        nextAction(player, new Place());
    }
}

class Place extends PlayerAction {
    @Override
    int placeCard(Player player, Card card, Side side, Position position)
            throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        assert (player.has(card));
        player.getPlayground().placeCard(card.getFace(side), position);
        player.discard(card);

        nextAction(player, new Draw());
        return player.getPoints();
    }

    @Override
    boolean isSetupFinished() {
        return true;
    }
}

class Draw extends PlayerAction {


    private void addNewCard(Player player, Card card) {
        List<Card> cards = player.getCards();
        assert(cards.size() == 2);
        cards.add(card);
    }

    @Override
    void addCard(Player player, Card newCard) throws InvalidPlayerActionException {
        List<Card> cards = player.getCards();
        assert(cards.size() == 2);
        cards.add(newCard);

        nextAction(player, new Place());
    }

    @Override
    boolean isSetupFinished() {
        return true;
    }
}
