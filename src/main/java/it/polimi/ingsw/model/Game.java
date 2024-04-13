package it.polimi.ingsw.model;

import java.io.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.JsonDeserializer.PositionDeserializer;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.chat.ChatDatabase;
import it.polimi.ingsw.model.player.Player;

/**
 * Representation of the game.
 * Each player is recognised through the system by their username.
 */

public class Game {
    private final static String objectivePositionCardsPath = "/cards/objectivePositionCards.json";
    private final static String objectiveResourceCardsPath = "/cards/objectiveResourceCards.json";
    private final static String startingCardsPath = "/cards/startingCards.json";
    private final static String goldenCardsPath = "/cards/goldenCards.json";

    private Deck resourceCards;
    private Deck goldenCards;
    private List<ObjectiveCard> ObjectiveCardDeck;
    private List<Card> startingCards;
    private List<ObjectiveCard> objectiveCards;
    private List<Front> faceUpCards; // todo. immutable
    private List<ObjectiveCard> commonObjects; // todo. immutable

    private enum GamePhase {
        NOT_STARTED,
        ONGOING,
        LAST_TURN,
        ENDED
    }

    GamePhase phase;

    private int numRequiredPlayers;
    private int currentPlayer; // index in the current player list.

    private List<Player> players;

    private ChatDatabase chatDatabase;

    // persistence
    // todo. define attribute to abstract access to the disk in order to save the
    // current status

    public Game(Player creator, int numPlayersToStart) throws IllegalArgumentException {
        this.objectiveCards = createObjectiveCards();
        this.startingCards = createPlayableCard(startingCardsPath);

    }

    /**
     * adds new player to the game, if possible.
     * 
     * @param newPlayer player to add
     * @return false if <code>newPlayer</code> contains a valid reference but their
     *         username has already in use or the game has already started.
     * @throws IllegalArgumentException if <code>newPlayer</code> is a null
     *                                  reference
     */
    // generic method to add new player. throws an exception for invalid players
    public boolean addPlayer(Player newPlayer) throws IllegalArgumentException {
        return true;
    }

    public void assignColor(String username, Color color) {

    }

    /**
     *
     */
    public void chooseObjectiveCard(ObjectiveCard chosenObjective) {

    }

    // todo. (maybe factor away if none need the information externally.
    // the game status is controlled by the model only, the other components
    // (Controller+View) receives information by the model
    boolean canStart() {
        return false;
    }

    // we can simply send a message containing the receiver and the event
    private List<Player> activePlayers() {
        return null;
    }

    // maybe done automatically by the model at the end of each turn
    void placeFaceUpCards() {

    }

    // todo(better name)
    public boolean isReachedTwenty() {
        return false;
    }

    public void drawFromRsourceDeck() {

    }

    public void drawFromGoldenDeck() {

    }

    // dynamic type passed by the controller
    public void placeCard(Face face) {

    }

    // handle next turn, skipping inactive players
    private void nextTurn() {

    }

    private void startLastTurn() {

    }

    /**
     * Deserializes objective cards from resources
     * 
     * @return the list of objective cards
     */
    private List<ObjectiveCard> createObjectiveCards() {
        /* json as streams, so even after jar build it can retrieve the correct file */
        InputStream objectiveResourceStream = this.getClass()
                .getResourceAsStream(objectiveResourceCardsPath);
        InputStream objectivePositionStream = this.getClass()
                .getResourceAsStream(objectivePositionCardsPath);

        //todo: handle null streams
        Reader objectiveResourceReader = new BufferedReader(new InputStreamReader(objectiveResourceStream));
        Reader objectivePositionReader = new BufferedReader(new InputStreamReader(objectivePositionStream));

        Gson gson = new GsonBuilder().registerTypeAdapter(Position.class, new PositionDeserializer()).create();

        List<ObjectiveResourceCard> objectiveResourceCards = gson.fromJson(objectiveResourceReader,
                new TypeToken<List<ObjectiveResourceCard>>() {
                }.getType());
        List<ObjectivePositionCard> objectivePositionCards = gson.fromJson(objectivePositionReader,
                new TypeToken<List<ObjectivePositionCard>>() {
                }.getType());

        List<ObjectiveCard> result = new ArrayList<>(objectiveResourceCards);
        result.addAll(objectivePositionCards);
        Collections.shuffle(result);

        return result;
    }

    /**
     * Creates any Card list
     * @param relativePath of the resource to instance
     * @return the list of the cards created from resource's json
     */
    private List<Card> createPlayableCard(String relativePath){
        /* json as streams, so even after jar build it can retrieve the correct file */
        InputStream cardStream = this.getClass()
                .getResourceAsStream(relativePath);
        //todo: handle null streams
        Reader cardReader = new BufferedReader(new InputStreamReader(cardStream));

        Gson gson = new Gson();

        List<Card> cards = gson.fromJson(cardReader,
                new TypeToken<List<Card>>() {
                }.getType());

        Collections.shuffle(cards);

        return cards;
    }

    // todo(needed). add method to know whether the client has disconnected and
    // notify all clients
}

/**
 * Custom deserializer for Corner:
 * uses Corner constructor because json only has kingdom as value
 */
