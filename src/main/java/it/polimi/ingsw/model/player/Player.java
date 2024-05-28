package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.player.action.ChooseStarter;
import it.polimi.ingsw.model.player.action.PlayerAction;
import it.polimi.ingsw.model.player.action.PlayerState;

import java.util.*;
import java.util.stream.Stream;

/**
 * Representation of the player's state.
 */
public class Player {

    //attributes
    /**
     * Indicates the username of the player.
     */
    private String username;

    /**
     * Represents the playground in which the actions are performed.
     */
    private Playground playground;

    /**
     * Represents the player's token color.
     */
    private PlayerColor colour;

    /**
     * Specifies whether the player is connected or not.
     */
    private boolean isConnected;

    /**
     * Represents the player's starter card.
     */
    private Card starter;

    /**
     * Represents the list of cards in player's possession, excluding the secret objective card and the starter card.
     */
    private List<Card> cards;

    /**
     * Represents the list of objective cards in player's possession.
     */
    private List<ObjectiveCard> objectiveCards;

    /**
     * Represents the current player's PlayerAction.
     */
    private PlayerAction playerAction;

    private int numSatisfiedObjectives;

    /**
     * Specifies if the player is active or not.
     */
    private boolean isActive;


    /**
     * Constructs a player using the username, token color, starter card, list of objective cards to choose and a list
     * with all other cards to be in player's possession provided.
     *
     * @param username           of the player.
     * @param starter            card.
     * @param cards              of the player.
     * @param objectivesToChoose list.
     */
    public Player(String username,
                  Card starter,
                  List<Card> cards,
                  List<ObjectiveCard> objectivesToChoose) {
        int numCards = 3, numObjectives = 2;

        assert (cards.size() == numCards
                && objectivesToChoose.size() == numObjectives);

        this.username = username;
        this.cards = cards;
        this.starter = starter;

        this.objectiveCards = objectivesToChoose;

        // it will be set to true by the add
        this.isConnected = false;
        this.playground = new Playground();

        this.playerAction = new ChooseStarter();
        this.numSatisfiedObjectives = 0;
    }

    //methods


    /**
     * Return the current points present in the playground.
     *
     * @return current points.
     */
    public int getPoints() {
        return playground.getPoints();
    }


    /**
     * Returns the username of the player.
     *
     * @return username.
     */
    public String getUsername() {
        return username;
    }


    /**
     * Returns the current PlayerAction state of the player.
     *
     * @return PlayerAction.
     */
    public PlayerAction getPlayerAction() {
        return playerAction;
    }

    public ObjectiveCard getObjective() {
        return objectiveCards.getFirst();
    }

    /**
     * Returns the available positions present in the playground.
     *
     * @return a list with the available positions present in the playground.
     */
    public List<Position> getAvailablePositions() {
        return playground.getAvailablePositions();
    }

    /**
     * Sets network <code>status</code> of this player.
     *
     * @param networkStatus true if the player is connected, false otherwise.
     */
    public void setNetworkStatus(boolean networkStatus) {
        this.isConnected = networkStatus;
    }


    /**
     * Tests if the player is connected or not.
     *
     * @return true if the player is connected, false otherwise.
     */
    public boolean isConnected() {
        return isConnected;
    }

    // Returns true iff the player has the specified <code>card</code>.

    /**
     * Tests if the player has the specified card.
     *
     * @param card to be tested.
     * @return true if the player has the specified card, false otherwise.
     */
    public boolean has(Card card) {
        return cards.contains(card);
    }


    /**
     * Returns the card with the specified front and back ids.
     *
     * @param frontId the id of the card's front.
     * @param backId  the id of the card's back.
     * @return the card matching the two ids.
     * @throws IllegalArgumentException if there's no card matching the provided ids.
     */
    public Card getCard(int frontId, int backId) throws InvalidCardIdException {
        return cards
                .stream()
                .filter(c -> c.getFace(Side.FRONT).getId() == frontId && c.getFace(Side.BACK).getId() == backId)
                .findAny()
                .orElseThrow(InvalidCardIdException::new);
    }

    // discard the card from the player's hand

    /**
     * Discards the card from the player's hand.
     *
     * @param card to be discarded.
     */
    public void discard(Card card) {
        assert (cards.remove(card)); // the card must exist
    }


    /**
     * Sets the PlayerAction state of the PlayerAction
     *
     * @param nextPlayerAction to be set.
     */
    public void setAction(PlayerAction nextPlayerAction) {
        playerAction = nextPlayerAction;
    }


    /**
     * Returns the playground to the player.
     *
     * @return the playground.
     */
    public Playground getPlayground() {
        return playground;
    }


    /**
     * Returns player's objective cards.
     *
     * @return a list of objective cards.
     */
    public List<ObjectiveCard> getObjectives() {
        return objectiveCards;
    }

    public PlayerColor getColour() {
        return colour;
    }

    /**
     * Sets <code>this</code>' color.
     *
     * @param color to set
     */
    public void setColor(PlayerColor color) {
        this.colour = color;
    }

    /**
     * Returns player's cards, excluding the objective and starter cards.
     *
     * @return a list of cards.
     */
    public List<Card> getCards() {
        return cards;
    }

    public Card getStarter() {
        return starter;
    }

    /**
     * Sets the player's color to <code>color</code>.
     *
     * @param color the color to set
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    public void assignColor(PlayerColor color) throws InvalidPlayerActionException {
        playerAction.assignColor(this, color);
    }

    /**
     * Places the private player's objective.
     *
     * @param chosenObjective to be added.
     * @throws InvalidPlayerActionException if the player has already chosen the secret objective.
     */
    public void placeObjectiveCard(int chosenObjective) throws InvalidPlayerActionException {
        playerAction.placeObjectiveCard(this, chosenObjective);
    }

    /**
     * Places the starter card on the specified side.
     *
     * @param side of the starter
     */
    public void placeStarter(Side side) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        playerAction.placeCard(this, starter, side, new Position(0, 0));
    }

    /**
     * Places the side of the card at the specified position.
     *
     * @param card     to place
     * @param side     of the card to be placed
     * @param position describing where to place the face.
     * @throws InvalidPlayerActionException if the player cannot place the card.
     */
    public int placeCard(Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        return playerAction.placeCard(this, card, side, position);
    }


    /**
     * Tests if the player has finished the set-up.
     *
     * @return true if the player has finished the set-up, false otherwise.
     */
    public boolean isSetupFinished() {
        return playerAction.getPlayerState() == PlayerState.Place
                || playerAction.getPlayerState() == PlayerState.Draw;
    }

    /**
     * Adds a new card to this player.
     *
     * @param newCard to add.
     * @throws InvalidPlayerActionException if the player cannot perform such  action.
     *                                      For example, the player has not already decided which card to place, thus they're not allowed to draw.
     */
    public void addCard(Card newCard) throws InvalidPlayerActionException {
        playerAction.addCard(this, newCard);
    }

    public int getNumSatisfiedObjectives() {
        return this.numSatisfiedObjectives;
    }

    /**
     * Calculates extra points from objective cards.
     *
     * @param commonObjectives the list of common objectives in the game.
     * @return the extra points reached.
     */
    public int calculateExtraPoints(List<ObjectiveCard> commonObjectives) {
        List<ObjectiveCard> objectives = new ArrayList<>(Stream.concat(commonObjectives.stream(), objectiveCards.stream()).toList());
        assert objectives.size() == 3;

        int extraPoints = 0;
        for (ObjectiveCard objective : objectives) {
            extraPoints = objective.calculatePoints(playground);
            if (extraPoints > 0) {
                ++numSatisfiedObjectives;
            }
        }

        return extraPoints;
    }
}

