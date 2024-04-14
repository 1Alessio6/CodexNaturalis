package it.polimi.ingsw.model;

import java.io.*;
import java.lang.reflect.Type;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.JsonDeserializer.PositionDeserializer;
import it.polimi.ingsw.model.board.Position;
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
    private final static String objectivePositionCardsPath = "/cards/objectivePositionCards.json";
    private final static String objectiveResourceCardsPath = "/cards/objectiveResourceCards.json";
    private final static String startingCardsPath = "/cards/startingCards.json";
    private final static String goldenCardsPath = "/cards/goldenCards.json";
    private Deck resourceCards;
    private Deck goldenCards;
    private List<ObjectiveCard> ObjectiveCardDeck;
    private List<Card> startingCards;
    private List<ObjectiveCard> objectiveCards;
    private List<ObjectiveCard> commonObjects; // todo. immutable
    private List<Card> faceUpCards;


    private enum GamePhase {
        NOT_STARTED,
        ONGOING,
        LAST_TURN,
        ENDED
    }

    GamePhase phase;

    private int numRequiredPlayers;
    private int currentPlayerIdx; // index in the current player list.
    private boolean isFinished;
    private List<Player> players;
    GameState gameState;

    // Advanced Features
    private ChatDatabase chatDatabase; // todo. maybe implement it outside

    public Game(Player creator, int numPlayersToStart) throws IllegalArgumentException {
        this.objectiveCards = createObjectiveCards();
        this.startingCards = createPlayableCard(startingCardsPath);
    }

    // methods

    // methods needed by the GameState's
    void setStatus(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * adds new player to the game, if possible.
     *
     * @param newPlayer player to add
     * @return false if <code>newPlayer</code> contains a valid reference but their
     * username has already in use or the game has already started.
     * @throws IllegalArgumentException if <code>newPlayer</code> is a null
     *                                  reference
     */
    // generic method to add new player. throws an exception for invalid players
    public boolean addPlayer(Player newPlayer) throws IllegalArgumentException {
        return true;
    }

    int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public void assignColor(String username, Color color) {

    }

    void setCurrentPlayerIdx(int newCurrentPlayerIdx) {
        assert (0 <= newCurrentPlayerIdx && newCurrentPlayerIdx < players.size());
        currentPlayerIdx = newCurrentPlayerIdx;
    }

    private boolean isValidIdx(int idx) {
        return 0 <= idx && idx < players.size();
    }


    // todo. (maybe factor away if none need the information externally.
    // the game status is controlled by the model only, the other components
    // (Controller+View) receives information by the model
    boolean canStart() {
        return false;
    }

    // maybe done automatically by the model at the end of each turn
    void placeFaceUpCards() {

    }

    // todo(better name)
    public boolean isReachedTwenty() {
        return false;
    }

    // handle next turn, skipping inactive players
    private void nextTurn() {

    }

    private void startLastTurn() {

    }

    void setFinished() {
        isFinished = true;
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

    // get and replace (if possible) the faceUp card at index faceUpCardIdx.
    Card getFaceUpCard(int faceUpCardIdx) {
        assert (isValidIdx(faceUpCardIdx));

        Card card = faceUpCards.get(faceUpCardIdx);

        // replace the card if possible (at least one deck is not empty)
        List<Deck<Card>> decks = Arrays.asList(resourceCards, goldenCards);
        int deckIdx = faceUpCardIdx <= 1 ? 0 : 1;

        Card replacement = null;

        try {
            if (!decks.get(deckIdx).isEmpty()) {
                replacement = decks.get(deckIdx).draw();
            } else {
                replacement = decks.get((deckIdx + 1) % 2).draw();
            }
        } catch (EmptyDeckException e) {
            // the replacement may be invalid if both decks are empty, so there's no exception to throw
        }

        faceUpCards.set(faceUpCardIdx, replacement); // null if there's none card for replacement

        return card;
    }


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
                                startingCards.draw(),
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


    /**
     * Constructs a game using the information stored in <code>gameBeforeCrash</code>.
     * In this way is possible to recover the game's status before the last crash.
     *
     * @param gameBeforeCrash the game's status before crashing.
     */
    public Game(Game gameBeforeCrash) {

        this.resourceCards = gameBeforeCrash.resourceCards;
        this.goldenCards = gameBeforeCrash.goldenCards;

        this.startingCards = gameBeforeCrash.startingCards;

        this.objectiveCards = gameBeforeCrash.objectiveCards;

        this.faceUpCards = gameBeforeCrash.faceUpCards;

        this.commonObjects = gameBeforeCrash.commonObjects;


        this.numRequiredPlayers = gameBeforeCrash.numRequiredPlayers;
        this.currentPlayerIdx = gameBeforeCrash.currentPlayerIdx;
        this.isFinished = gameBeforeCrash.isFinished;

        this.players = gameBeforeCrash.players;
        this.gameState = gameBeforeCrash.gameState;

    }


    /*
        The currentPlayerIdx is updated every time a request from outside about the players' turn comes.
        This function, whose return type needs to be defined, will check if the game is finished, if so it will send a notification to all players; otherwise
        it will check  whether the oldCurrentPlayer has drawn a card. If the player has not done that, an automatic drawn is done for them and the new player is selected.
        We're assuming the request has taken into account the requirements: no action when there's only one player
        and the game stops when no one is active (a timer is created outside).
     */

    /**
     * Returns the current player.
     *
     * @return username of the next current player.
     * @throws SuspendedGameException if there's less than 2 connected players.
     */
    public String getCurrentPlayer() throws SuspendedGameException {
        if (getActivePlayers().size() <= 1) {
            throw new SuspendedGameException("At least two connected players are required for the game to continue");
        }

        boolean foundNextPlayer = false;

        currentPlayerIdx = (currentPlayerIdx + 1) % numRequiredPlayers;
        for (int i = 0; !foundNextPlayer && i < numRequiredPlayers; ++i) {
            if (players.get(currentPlayerIdx).isConnected()) {
                foundNextPlayer = true;
            }

            gameState.skipTurn(this);
            currentPlayerIdx = (currentPlayerIdx + 1) % numRequiredPlayers;
        }

        assert foundNextPlayer;

        return players.get(currentPlayerIdx).getUsername();
    }


    /**
     * Sets the player's network status to <code>networkStatus</code>.
     *
     * @param username      of the player.
     * @param networkStatus of the player to be set.
     */
    public void setNetworkStatus(String username, boolean networkStatus) {
        players.stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .ifPresent(p -> p.setNetworkStatus(networkStatus));
    }

    /*
     *  NOTE. We're assuming all methods are called before having requested the getCurrentPlayerIdx
     */

    /*
     * NOTE. Exceptions are handled by the controller
     */

    private Player getUsername(String username) throws IllegalArgumentException {
        return getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().orElseThrow(() -> new IllegalArgumentException("Username not found"));
    }

    /**
     * Places the starter on the specified side.
     *
     * @param username of the player
     * @param side     of the starter
     * @throws InvalidPlayerActionException if the player cannot perform the operation. For example the player has already chosen the side.
     */
    public void placeStarter(String username, Side side) throws InvalidPlayerActionException {
        try {
            gameState.placeStarter(this, getUsername(username), side);
        } catch (Playground.UnavailablePositionException | Playground.NotEnoughResourcesException ignored) {
            // the placement of the starter cannot cause problems
        }
    }

    /**
     * Places the secret objective from one of the two available.
     *
     * @param username        of the player.
     * @param chosenObjective the chosen objective.
     * @throws InvalidPlayerActionException if the player cannot perform the operation. For example the player has already chosen the objective.
     */
    public void placeObjectiveCard(String username, ObjectiveCard chosenObjective) throws InvalidPlayerActionException {
        gameState.placeObjectiveCard(this, getUsername(username), chosenObjective);
    }

    /**
     * Places the card on the side and position specified.
     *
     * @param username of the player.
     * @param card     to place.
     * @param side     of the card.
     * @param position in the playground.
     * @throws InvalidPlayerActionException            if the player cannot perform the operation.
     * @throws Playground.UnavailablePositionException if the position is not available. For example the player is trying to place the card in an already covered corner.
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card.
     */
    public void placeCard(String username, Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        gameState.placeCard(this, getUsername(username), card, side, position);
    }

    /**
     * Draws from the resource cards deck
     *
     * @param username of the player
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException           if the deck is empty.
     */
    public void drawFromResourceDeck(String username) throws InvalidPlayerActionException, EmptyDeckException {
        gameState.drawFromResourceDeck(this, getUsername(username));
    }

    /**
     * Draws from the golden cards deck
     *
     * @param username of the player
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException           if the deck is empty.
     */
    public void drawFromGoldenDeck(String username) throws InvalidPlayerActionException, EmptyDeckException {
        gameState.drawFromGoldenDeck(this, getUsername(username));
    }

    /**
     * Draws from one of the available face up cards.
     *
     * @param username      of the player.
     * @param faceUpCardIdx specifying the face up card.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    public void drawFromFaceUpCards(String username, int faceUpCardIdx) throws InvalidPlayerActionException {
        gameState.drawFromFaceUpCards(this, getUsername(username), faceUpCardIdx);
    }

    /**
     * Draws automatically to complete the current player's turn.
     * It may happen if the current player disconnects after placing a card but before drawing the new one.
     *
     * @param username of the player.
     */
    public void automaticDraw(String username) {
        assert players.get(currentPlayerIdx).getUsername().equals(username); // the automatic draw is for the current player only


        Random rand = new Random();
        int methodOfDraw = rand.nextInt(4);
        Player currentPlayer = players.get(currentPlayerIdx);

        if (!players.get(currentPlayerIdx).isConnected()) {
            // The current player may have already drawn causing exceptions thrown; such exceptions will be not considered.
            try {
                if (methodOfDraw == 0) {
                    if (!resourceCards.isEmpty()) {
                        gameState.drawFromResourceDeck(this, currentPlayer);
                    }
                } else if (methodOfDraw == 1) {
                    if (!goldenCards.isEmpty()) {
                        gameState.drawFromGoldenDeck(this, currentPlayer);
                    }
                } else {
                    gameState.drawFromFaceUpCards(this, currentPlayer, rand.nextInt(5));
                }
            } catch (Exception e) {

            }
        }
    }

    private List<Player> getActivePlayers() {
        return players.stream()
                .filter(Player::isConnected)
                .toList();
    }

    // todo(needed). add method to know whether the client has disconnected and
    // notify all clients

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
    private List<Card> createPlayableCard(String relativePath) {
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

    /**
     * Custom deserializer for Corner:
     * uses Corner constructor because json only has kingdom as value
     */
    class CornerDeserializer implements JsonDeserializer<Corner> {
        @Override
        public Corner deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new Corner(Symbol.valueOf(json.getAsString()));
        }
    }
}

