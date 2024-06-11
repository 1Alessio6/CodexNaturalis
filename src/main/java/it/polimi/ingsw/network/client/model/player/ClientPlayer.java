package it.polimi.ingsw.network.client.model.player;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;

/**
 * Representation of the player in the client's side.
 */
public class ClientPlayer implements Serializable {

    private final String username;

    private ClientPlayground playground;

    private PlayerColor color;

    private boolean isConnected;

    private ClientCard starterCard;

    // cards in hand
    private List<ClientCard> playerCards;

    private List<ClientObjectiveCard> objectiveCards;

    /**
     * Constructs a clientPlayer using the <code>username</code> provided.
     *
     * @param username the username of the player.
     */
    public ClientPlayer(String username) {
        this.username = username;
    }

    //getter method needed for the view

    /**
     * Constructs a clientPlayer using the <code>username</code>, <code>starterCard</code>, <code>playerCards</code>
     * and <code>objectiveCards</code> provided.
     *
     * @param username       the username of the player.
     * @param starterCard    the starter card of the player.
     * @param playerCards    the player's hand.
     * @param objectiveCards the objective cards to choose from.
     */
    public ClientPlayer(String username, ClientCard starterCard, List<ClientCard> playerCards, List<ClientObjectiveCard> objectiveCards) {
        this.username = username;
        this.starterCard = starterCard;
        this.playerCards = playerCards;
        this.objectiveCards = objectiveCards;
    }

    /**
     * Constructs a clientPlayer using the <code>player</code>.
     *
     * @param player the representation of the player.
     */
    public ClientPlayer(Player player) {
        username = player.getUsername();
        playground = new ClientPlayground(player.getPlayground());
        color = player.getColour();
        isConnected = player.isConnected();
        starterCard = new ClientCard(player.getStarter());
        playerCards = new ArrayList<>();
        for (Card c : player.getCards()) {
            playerCards.add(new ClientCard(c));
        }
        objectiveCards = new ArrayList<>();
        for (ObjectiveCard c : player.getObjectives()) {
            objectiveCards.add(new ClientObjectiveCard(c));
        }
    }

    public int getAmountResource(Symbol s) {
        return this.getPlayground().getResources().get(s);
    }

    /**
     * Adds a <code>card</code> to the player's hand.
     *
     * @param card the card to be added.
     */
    public void addPlayerCard(ClientCard card) {
        playerCards.add(card);
    }

    public ClientPlayground getPlayground() {
        return playground;
    }

    public String getUsername() {
        return username;
    }

    public PlayerColor getColor() {
        return color;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public ClientCard getStarterCard() {
        return starterCard;
    }

    public ClientCard getPlayerCard(int cardHandPosition) {
        return playerCards.get(cardHandPosition);
    }

    public List<ClientObjectiveCard> getObjectiveCards(){return objectiveCards;}

    public List<ClientCard> getPlayerCards(){return playerCards;}

    //setter methods

    public void setNetworkStatus(boolean networkStatus) {
        this.isConnected = networkStatus;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public void setStarterCard(ClientCard starterCard) {
        this.starterCard = starterCard;
    }

    public void setPlayerCard(ClientCard card, int cardHandPosition) {
        this.playerCards.set(cardHandPosition, card);
    }

    /**
     * Updates objective cards with the <code>chosenObjectiveCard</code>.
     *
     * @param chosenObjectiveCard the objective card chosen by the player.
     */
    public void updateObjectiveCard(ClientObjectiveCard chosenObjectiveCard){
        objectiveCards = new ArrayList<>();
        objectiveCards.add(chosenObjectiveCard);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        return this.username.equals(((ClientPlayer) obj).getUsername());
    }

    /**
     * Removes a <code>cardToRemove</code> from the player's hand.
     *
     * @param cardToRemove the card to remove.
     */
    public void removePlayerCard(ClientCard cardToRemove){
        playerCards.removeIf(card -> cardToRemove.getBackId() == card.getBackId() && cardToRemove.getFrontId() == card.getFrontId());
    }
}
