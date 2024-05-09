package it.polimi.ingsw.model.player.action;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

/**
 * A class containing all methods representing possible player's action.
 * The fsm is
 * ChooseStarter -> ChooseColor -> ChooseObjective -> Place -> Draw -> Place
 * NOTE. Each Player Action is independent of the game's state, it only models which operation the player can do provided
 * the right context.
 */

public abstract class PlayerAction {
    /**
     * Allows a player to place a card on the side and position specified.
     *
     * @param player   who performs the placement.
     * @param card     to place.
     * @param side     of the card.
     * @param position in the playground.
     * @throws InvalidPlayerActionException            if the player cannot perform the operation.
     * @throws Playground.UnavailablePositionException if the position is not available.
     * @throws Playground.NotEnoughResourcesException  if the player's resources are not enough to place the card.
     */
    public int placeCard(Player player, Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        throw new InvalidPlayerActionException("Player cannot place a card");
    }

    /**
     * Sets the player's color to <code>color</code>.
     *
     * @param player the player who has chose the color
     * @param color the color to set
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    public void assignColor(Player player, PlayerColor color) throws InvalidPlayerActionException {
        throw new InvalidPlayerActionException("Player cannot place a card");
    }

    /**
     * Allows the player to place the secret objective card.
     *
     * @param player          who performs the placement.
     * @param chosenObjective card.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    public void placeObjectiveCard(Player player, int chosenObjective) throws InvalidPlayerActionException {
        throw new InvalidPlayerActionException("Player has already choose an objective");
    }


    /**
     * Adds a new card to the player's card list.
     *
     * @param player  to which the card is added.
     * @param newCard to be added.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    public void addCard(Player player, Card newCard) throws InvalidPlayerActionException {
        throw new InvalidPlayerActionException("Player cannot draw");
    }

    /**
     * Updates the state of the Player Action.
     *
     * @param player     to whom the update is applied.
     * @param nextAction of the Player Action.
     */
    public void nextAction(Player player, PlayerAction nextAction) {
        player.setAction(nextAction);
    }

    public abstract PlayerState getPlayerState();
}






