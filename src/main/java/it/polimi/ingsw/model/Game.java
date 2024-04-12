package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.chat.ChatDatabase;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;

import java.util.*;


/**
 * Representation of the game.
 * Each player is recognised through the system by their username.
 */

public class Game {
    private Deck<Card> resourceCards;
    private Deck<Card> goldenCards;

    private Deck<Card> starterCards;
    private Deck<ObjectiveCard> objectiveCards;

    private List<Card> faceUpCards;

    private List<ObjectiveCard> commonObjects;

    private int numRequiredPlayers;
    private int currentPlayerIdx; // index in the current player list.
    private boolean isFinished;

    private List<Player> players;

    // replace using the state pattern
    GameState gameState;

    // Advanced Features
    private ChatDatabase chatDatabase;

    // persistence
    // todo. define attribute to abstract access to the disk in order to save the current status


    // todo. replace with specific observers
    private List<Observer> observers;

    // methods


    // methods needed by the GameState's
    void setStatus(GameState gameState) {
        this.gameState = gameState;
    }

    int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    void setCurrentPlayerIdx(int newCurrentPlayerIdx) {
        assert (0 <= newCurrentPlayerIdx && newCurrentPlayerIdx < players.size());
        currentPlayerIdx = newCurrentPlayerIdx;
    }

    private boolean isValidIdx(int idx) {
        return 0 <= idx && idx < players.size();
    }

    int getNextPlayerIdx(int oldCurrentPlayerIdx) {
        int numPlayers = players.size();

        assert (isValidIdx(oldCurrentPlayerIdx));
        int res = (oldCurrentPlayerIdx + 1) % numPlayers;
        assert (isValidIdx(oldCurrentPlayerIdx));

        return res;
    }

    List<Card> getFaceUpCards() {
        return faceUpCards;
    }

    void setFinished() {
        isFinished = true;
    }

    // todo. make it more readable
    // Returns the deck containing the type of the card drawn by the player.
    Deck<Card> getDeckForReplacement(int faceUpCardIdx) {
        if (faceUpCardIdx <= 1) {
            return resourceCards;
        } else {
            return goldenCards;
        }
    }

    List<Player> getPlayers() {
        return players;
    }

    Deck<Card> getResourceDeck() {
        return resourceCards;
    }

    Deck<Card> getGoldenDeck() {
        return goldenCards;
    }


    // Possible requests from outside

    // todo. check correctness
    public Game(List<String> usernames, List<Color> colors) {
        // todo. add method to load cards


        // rest of the code
        numRequiredPlayers = usernames.size();

        players = new ArrayList<>();
        try {
            commonObjects = Arrays.asList(objectiveCards.draw(), objectiveCards.draw());

            for (int i = 0; i < numRequiredPlayers; ++i) {
                players.add(
                        new Player(
                                usernames.get(i),
                                colors.get(i),
                                starterCards.draw(),
                                Arrays.asList(resourceCards.draw(), resourceCards.draw(), goldenCards.draw()),
                                Arrays.asList(objectiveCards.draw(), objectiveCards.draw())
                        )
                );
            }

        } catch (EmptyDeckException e) {
            e.printStackTrace();
        }

        gameState = new Setup();
        currentPlayerIdx = 0;
        isFinished = false;
        chatDatabase = new ChatDatabase();
    }

    /*
        The currentPlayerIdx is updated every time a request from outside about the players' turn comes.
        This function, whose return type needs to be defined, will check if the game is finished, if so it will send a notification to all players; otherwise
        it will check  whether the oldCurrentPlayer has drawn a card. If the player has not done that, an automatic drawn is done for them and the new player is selected.
        We're assuming the request has taken into account the requirements: no action when there's only one player
        and the game stops when no one is active (a timer is created outside).
     */

    // todo. implement method to select the next current player. They must be a player whose status has set to active
    // when the function was called. If the selected player disconnected after the request was sent, another query would be sent,
    // but it's not something the Game has to consider: it only refers to the situation at the moment of the call.
    int selectNextCurrentPlayer() {
        // They may disconnect before drawing a card, an automatic draw is done for them
        if (!players.get(currentPlayerIdx).isConnected()) {
            // todo. choose randomly from which source draw a card.
        }

        // todo. while skipping inactive players call the skip function in gameState to let gameState know if an additional turn starts.

        return -1;
    }

    public void setNetworkStatus(String username, boolean networkStatus) {

        players.stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .ifPresent(p -> p.setNetworkStatus(networkStatus));
    }

    ///**
    // * Connects a previously disconnected player with <code>username</code> to the game.
    // *
    // * @param username of the player to connect
    // */
    //public void connectPlayer(String username) {
    //    setNetworkStatus(username, true);
    //}

    ///**
    // * Disconnects the player with <code>username</code> to the game.
    // *
    // * @param username of the player to connect
    // */
    //public void disconnectPlayer(String username) {
    //    setNetworkStatus(username, false);
    //}


    /*
     *  NOTE. We're assuming all methods are called before having requested the list of active players, which in turns call the method to select the next currentPlayer.
     */

    public void placeStarter(String username, Side side) {
        try {
            gameState.placeStarter(this, username, side);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void placeObjectiveCard(String username, ObjectiveCard chosenObjective) {
        try {
            gameState.placeObjectiveCard(this, username, chosenObjective);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void placeCard(String username, Card card, Side side, Position position) {
        try {
            gameState.placeCard(this, username, card, side, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawFromResourceDeck(String username) {
        try {
            gameState.drawFromResourceDeck(this, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawFromGoldenDeck(String username) {
        try {
            gameState.drawFromGoldenDeck(this, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawFromFaceUpCards(String username, int faceUpCardIdx) {
        try {
            gameState.drawFromFaceUpCards(this, username, faceUpCardIdx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Player> getActivePlayers() {
        return players.stream()
                .filter(Player::isConnected)
                .toList();
    }

}

