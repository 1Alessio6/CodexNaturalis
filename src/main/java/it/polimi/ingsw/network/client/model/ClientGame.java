package it.polimi.ingsw.network.client.model;

import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamephase.GamePhase;
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

/**
 * Representation of the game in the client's side.
 */
public class ClientGame implements Serializable {

    private List<ClientCard> faceUpCards;
    private List<ClientObjectiveCard> commonObjects;
    private int currentPlayerIdx; // index in the current player list.
    private List<ClientPlayer> players;
    private List<Message> messages;
    private ClientBoard clientBoard;
    private GamePhase currentPhase;
    private boolean isGameActive;

    /**
     * Constructs a game with no parameters provided.
     */
    public ClientGame() {
        this.players = new ArrayList<>();
    }

    /**
     * Checks if the game is active.
     *
     * @return true if the game is active, false otherwise.
     */
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

    /**
     * Constructor used to copy the game from server to client
     * @param game on the server
     */
    public ClientGame(Game game) {
       // System.err.println("Creating a copy of the game");
        this.currentPlayerIdx = game.getCurrentPlayerIdx();
        this.players = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            ClientPlayer clientPlayer = new ClientPlayer(player);
            this.players.add(clientPlayer);
        }
        this.clientBoard = new ClientBoard(game.getFaceUpCards(), game.getCommonObjectives(), game.getTopDeckBack(DeckType.GOLDEN), game.getTopDeckBack(DeckType.RESOURCE));

        this.messages = game.getMessages();

        this.isGameActive = game.isActive();
        this.currentPhase = game.getPhase();
    }

    /**
     * Constructor used update players in lobby
     * @param usernames of the players that are in the lobby now
     */
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

    /**
     * Verifies if there is a face up card in the <code>index</code>.
     *
     * @param index on which the availability of the face up card is to be checked.
     * @return true if the slot is empty, false otherwise.
     */
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

    /**
     * Checks if a <code>username</code> is among the players in the current game.
     *
     * @param username of the player.
     * @return true if the <code>username</code> is among the players, false otherwise.
     */
    public boolean containsPlayer(String username) {
        return players.contains(getPlayer(username));
    }

    /**
     * Adds a player to the game
     *
     * @param username of the player to be added.
     */
    public void addPlayer(String username) {
        players.add(new ClientPlayer(username));
    }

    public ClientPlayground getPlaygroundByUsername(String username) {
        return getPlayer(username).getPlayground();
    }

    public ClientPlayer getCurrentPlayer() {
        return players.get(currentPlayerIdx);
    }

    /**
     * Adds a <code>newMessage</code> sent by author.
     *
     * @param newMessage to be added.
     */
    public void addMessage(Message newMessage) {
        messages.add(newMessage);
    }

}


