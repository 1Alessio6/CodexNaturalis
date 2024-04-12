package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;

import java.util.*;


// todo. define equals method for Player based on username equality

// designPattern(State, Player) == Contextor
// The network status is not modelled as a state to avoid storing information related to the previous state to recovery the last state
public class Player {

    //attributes

    private String username;
    private Playground playground;
    private Color colour;
    private boolean isConnected;
    private Card starter;
    private List<Card> cards;
    private List<ObjectiveCard> objectiveCards;
    private PlayerAction playerAction;

    private boolean isActive;

    public Player(String username,
           Color color,
           Card starter,
           List<Card> cards,
           List<ObjectiveCard> objectivesToChoose) {
        int numCards = 3, numObjectives = 2;

        assert (cards.size() == numCards
                && objectivesToChoose.size() == numObjectives);

        this.username = username;
        this.colour = color;
        this.cards = cards;
        this.starter = starter;

        this.objectiveCards = objectivesToChoose;

        this.isConnected = true;
        this.playground = new Playground();
    }

    //methods

    public int getPoints() {
        return playground.getPoints();
    }

    public String getUsername() {
        return username;
    }

    public PlayerAction getPlayerAction() {
        return playerAction;
    }

    public List<Position> getAvailablePositions() {
        return playground.getAvailablePositions();
    }

    /**
     * Sets network <code>status</code> of this player.
     * @param networkStatus true if the player is connected, false otherwise.
     */
    public void setNetworkStatus(boolean networkStatus) {
        this.isConnected = networkStatus;
    }

    public boolean isConnected() {
        return isConnected;
    }

    // Returns true iff the player has the specified <code>card</code>.
    boolean has(Card card) {
        return cards.stream()
                .filter((c) -> {
                    return c.equals(card);
                })
                .toList()
                .isEmpty();
    }

    // discard the card from the player's hand
    void discard(Card card) {
        assert (cards.remove(card)); // the card must exist
    }

    void setAction(PlayerAction nextPlayerAction) {
        playerAction = nextPlayerAction;
    }

    Playground getPlayground() {
        return playground;
    }

    List<ObjectiveCard> getObjectives() {
        return objectiveCards;
    }

    List<Card> getCards() {
        return cards;
    }

    /**
     * Places the private player's objective.
     *
     * @param objectiveCard to be added.
     * @throws InvalidPlayerActionException if the player has already chosen the secret objective.
     */
    public void placeObjectiveCard(ObjectiveCard objectiveCard) throws InvalidPlayerActionException {
        playerAction.placeObjectiveCard(this, objectiveCard);
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

    public boolean isSetupFinished() {
        return playerAction.isSetupFinished();
    }

    /**
     * Adds a new card to this player.
     * @param newCard to add.
     * @throws InvalidPlayerActionException if the player cannot perform such  action.
     * For example, the player has not already decided which card to place, thus they're not allowed to draw.
     */
    public void addCard(Card newCard) throws InvalidPlayerActionException {
        playerAction.addCard(this, newCard);
    }

}

