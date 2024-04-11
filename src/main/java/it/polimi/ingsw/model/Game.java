package it.polimi.ingsw.model;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.card.Face;
import it.polimi.ingsw.model.card.Front;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.ObjectivePositionCard;
import it.polimi.ingsw.model.card.ObjectiveResourceCard;
import it.polimi.ingsw.model.chat.ChatDatabase;
import it.polimi.ingsw.model.player.Player;

/**
 * Representation of the game.
 * Each player is recognised through the system by their username.
 */

public class Game {
    private final static String objectivePositionCardsPath = "/cards/objectivePositionCards.json";
    private final static String objectiveResourceCardsPath = "/cards/objectiveResourceCards.json";

    private Deck resourceCards;
    private Deck goldenCards;
    private List<ObjectiveCard> ObjectiveCardDeck;
    private List<Card> starterCards;
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

        return result;
    }

    // todo(needed). add method to know whether the client has disconnected and
    // notify all clients
}

/**
 * Custom deserializer for Position:
 * assigns string values to x and y fields
 */
class PositionDeserializer implements JsonDeserializer<Position> {
    @Override
    public Position deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String[] positionString = json.getAsString().split(",");

        return new Position(Integer.parseInt(positionString[0]), Integer.parseInt(positionString[1]));
    }
}
