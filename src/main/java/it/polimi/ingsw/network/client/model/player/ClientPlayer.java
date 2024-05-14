package it.polimi.ingsw.network.client.model.player;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;

public class ClientPlayer {

    private final String username;

    private ClientPlayground playground;

    private PlayerColor color;

    private boolean isConnected;

    private ClientCard starterCard;

    // cards in hand
    private List<ClientCard> playerCards;

    private List<ClientObjectiveCard> objectiveCards;

    public ClientPlayer(String username) {
        this.username = username;
    }

    //getter method needed for the view

    public ClientPlayer(String username, ClientCard starterCard, List<ClientCard> playerCards, List<ClientObjectiveCard> objectiveCards) {
        this.username = username;
        this.starterCard = starterCard;
        this.playerCards = playerCards;
        this.objectiveCards = objectiveCards;
    }

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

    public void removePlayerCard(ClientCard cardToRemove){
        playerCards.removeIf(card -> cardToRemove.getBackId() == card.getBackId() && cardToRemove.getFrontId() == card.getFrontId());
    }
}
