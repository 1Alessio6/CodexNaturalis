package it.polimi.ingsw.network.client.model;

import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.model.board.ClientBoard;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientGame implements Serializable {

    private List<ClientCard> faceUpCards;
    private List<ClientObjectiveCard> commonObjects;
    private int currentPlayerIdx; // index in the current player list.
    private List<ClientPlayer> players;
    private List<Message> messages;
    private ClientBoard clientBoard;
    private GamePhase currentPhase;
    private boolean isGameActive;

    public ClientGame() {
        this.players = new ArrayList<>();
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public void setGameActive(boolean gameActive) {
        isGameActive = gameActive;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public List<ClientPlayer> getPlayers() {
        return players;
    }

    public ClientPlayer getPlayer(String username) {
        for (ClientPlayer player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    //todo update get main player


    public void setCurrentPlayerIdx(int currentPlayerIdx) {
        this.currentPlayerIdx = currentPlayerIdx;
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public List<ClientCard> getFaceUpCards() {
        return faceUpCards;
    }

    public List<ClientObjectiveCard> getCommonObjects() {
        return commonObjects;
    }

    public int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public ClientPlayer getPlayer(int numPlayer) {
        return players.get(numPlayer);
    }

    public List<Message> getMessages() {
        return messages;
    }
    public ClientBoard getClientBoard() {
        return clientBoard;
    }

    public ClientGame(Game game) {
        faceUpCards = new ArrayList<>();
        for (Card faceUpCard : game.getFaceUpCards()) {
            faceUpCards.add(new ClientCard(faceUpCard));
        }
        commonObjects = new ArrayList<>();
        for (ObjectiveCard commonObjective : game.getCommonObjectives()) {
            commonObjects.add(new ClientObjectiveCard(commonObjective));
        }
        currentPlayerIdx = game.getCurrentPlayerIdx();
        players = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            ClientPlayer clientPlayer = new ClientPlayer(player);
            players.add(clientPlayer);
        }
        clientBoard = new ClientBoard(game.getFaceUpCards(), game.getCommonObjectives(), game.getTopDeckBack(DeckType.GOLDEN), game.getTopDeckBack(DeckType.RESOURCE));

        messages = game.getMessages();

    }

    public ClientGame(List<String> usernames) {
        players = new ArrayList<>();
        for (String username : usernames) {
            players.add(new ClientPlayer(username));
        }
    }

    public boolean isGoldenDeckEmpty() {
        return clientBoard.isGoldenDeckEmpty();
    }

    public boolean isResourceDeckEmpty() {
        return clientBoard.isResourceDeckEmpty();
    }

    public ClientCard getFaceUpCard(int index) {
        return clientBoard.getFaceUpCards().get(index);
    }

    public boolean isFaceUpSlotEmpty(int index) {
        return getFaceUpCard(index) == null;
    }

    public Set<PlayerColor> getAlreadyTakenColors() {
        Set<PlayerColor> alreadyTakenColors = new HashSet<>();

        for (ClientPlayer player : players) {
            if (player.getColor() != null) {
                alreadyTakenColors.add(player.getColor());
            }
        }

        return alreadyTakenColors;
    }

    public Set<PlayerColor> getRemainingColors() {
        Set<PlayerColor> remainingColors = new HashSet<>();
        Set<PlayerColor> alreadyTakenColors = getAlreadyTakenColors();

        for (PlayerColor color : PlayerColor.values()) {
            if (!alreadyTakenColors.contains(color)) {
                remainingColors.add(color);
            }
        }

        return remainingColors;
    }

    public boolean containsPlayer(String username) {
        return players.contains(getPlayer(username));
    }

    public void addPlayer(String username) {
        players.add(new ClientPlayer(username));
    }

    public ClientPlayground getPlaygroundByUsername(String username) {
        return getPlayer(username).getPlayground();
    }

    public ClientPlayer getCurrentPlayer() {
        return players.get(currentPlayerIdx);
    }

    public void addMessage(Message newMessage) {
        messages.add(newMessage);
    }

}


