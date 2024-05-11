package it.polimi.ingsw.network.client.model.player;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;

import java.util.List;
import java.util.Map;

public class ClientPlayer {

    private final String username;

    private ClientPlayground playground;

    private PlayerColor color;

    private boolean isConnected;

    private boolean isCurrentPlayer;

    private ClientCard starterCard;

    // cards in hand
    private List<ClientCard> playerCards;

    private List<ClientCard> objectiveCards;

    private Map<Integer, Map<Symbol, Integer>> goldenFrontRequirements; //todo could be changed into an array list of map

    public ClientPlayer(String username) {
        this.username = username;
    }

    //getter method needed for the view

    public ClientPlayer(String username, ClientCard starterCard, List<ClientCard> playerCards, List<ClientCard> objectiveCards) {
        this.username = username;
        this.starterCard = starterCard;
        this.playerCards = playerCards;
        this.objectiveCards = objectiveCards;
    }

    public int getAmountResource(Symbol s) {
        return this.getPlayground().getResources().get(s);
    }

    public boolean isGoldenFront(int frontID) {
        return this.goldenFrontRequirements.containsKey(frontID);
    }

    public Map<Symbol, Integer> getGoldenFrontRequirements(int frontID) {
        assert (isGoldenFront(frontID));
        return this.goldenFrontRequirements.get(frontID);
    }

    public void addPlayerCard(ClientCard card){
        playerCards.add(card);
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    public void setIsCurrentPlayer(boolean currentPlayer) {
        isCurrentPlayer = currentPlayer;
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
}
