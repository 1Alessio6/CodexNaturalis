package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.chat.ChatDatabase;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;


/**
 * Representation of the game.
 * Each player is recognised through the system by their username.
 */

public class Game {
    private final static String goldenCardsPath = "/cards/goldenCards.json";
    private final static String resourceCardsPath = "/cards/resourceCards.json";
    private final static String startingCardsPath = "/cards/startingCards.json";
    private final static String objectivePositionCardsPath = "/cards/objectivePositionCards.json";
    private final static String objectiveResourceCardsPath = "/cards/objectiveResourceCards.json";

    private final Deck<Card> resourceCards;
    private final Deck<Card> goldenCards;
    private final Deck<Card> starterCards;
    private final Deck<ObjectiveCard> objectiveCards;

    private List<Card> faceUpCards;
    private List<ObjectiveCard> commonObjects;

    // private int numRequiredPlayers;
    private int currentPlayerIdx; // index in the current player list.
    private boolean isFinished;

    private List<Player> players;

    GameState gameState;

    // Advanced Features
    private ChatDatabase chatDatabase; // todo. maybe implement it outside

    /**
     * Creates game based on the lobby
     * @param users the map user:color of the lobby that wants to start the game
     */

    public Game(Map<String, Color> users) {
        this.resourceCards = new Deck<>(Deck.getPlayableCardListFromJson(resourceCardsPath));
        this.goldenCards = new Deck<>(Deck.getPlayableCardListFromJson(goldenCardsPath));
        this.starterCards = new Deck<>(Deck.getPlayableCardListFromJson(startingCardsPath));

        ArrayList<ObjectiveCard> objectiveCardList = new ArrayList<>();
        objectiveCardList.addAll(Deck.getObjectiveResourceCardListFromJson(objectiveResourceCardsPath));
        objectiveCardList.addAll(Deck.getObjectivePositionCardListFromJson(objectivePositionCardsPath));

        this.objectiveCards = new Deck<>(objectiveCardList);

        try {

            players = new ArrayList<>();
            for (Map.Entry<String, Color> entry : users.entrySet()) {
                List<Card> userHand = new ArrayList<>();
                userHand.add(resourceCards.draw());
                userHand.add(resourceCards.draw());
                userHand.add(goldenCards.draw());

                List<ObjectiveCard> userObjectives = new ArrayList<>();
                userObjectives.add(objectiveCards.draw());
                userObjectives.add(objectiveCards.draw());

                Card startingCard = starterCards.draw();
                players.add(new Player(
                    entry.getKey(),
                        entry.getValue(),
                        startingCard,
                        userHand,
                        userObjectives
                ));
            }
        } catch (EmptyDeckException e) {
            e.printStackTrace();
        }

        gameState = new Setup();
        currentPlayerIdx = 0;
        isFinished = false;
        chatDatabase = new ChatDatabase();
    }

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

    /**
     * Constructs a game using the information stored in <code>gameBeforeCrash</code>.
     * In this way is possible to recover the game's status before the last crash.
     *
     * @param gameBeforeCrash the game's status before crashing.
     */
    public Game(Game gameBeforeCrash) {
        this.resourceCards = gameBeforeCrash.resourceCards;
        this.goldenCards = gameBeforeCrash.goldenCards;

        this.starterCards = gameBeforeCrash.starterCards;

        this.objectiveCards = gameBeforeCrash.objectiveCards;

        this.faceUpCards = gameBeforeCrash.faceUpCards;

        this.commonObjects = gameBeforeCrash.commonObjects;

        // this.numRequiredPlayers = gameBeforeCrash.numRequiredPlayers;
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
     * @return the next current player.
     * @throws SuspendedGameException if there's less than 2 connected players.
     */
    public Player getCurrentPlayer() throws SuspendedGameException {
        if (getActivePlayers().size() <= 1) {
            throw new SuspendedGameException("At least two connected players are required for the game to continue");
        }

        boolean foundNextPlayer = false;
        int numRequiredPlayers = players.size();

        currentPlayerIdx = (currentPlayerIdx + 1) % numRequiredPlayers;
        for (int i = 0; !foundNextPlayer && i < numRequiredPlayers; ++i) {
            if (players.get(currentPlayerIdx).isConnected()) {
                foundNextPlayer = true;
            }

            gameState.skipTurn(this);
            currentPlayerIdx = (currentPlayerIdx + 1) % numRequiredPlayers;
        }

        assert foundNextPlayer;

        return players.get(currentPlayerIdx);
    }


    /**
     * Sets the player's network status to <code>networkStatus</code>.
     * @param username of the player.
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

    public Player getUserByUsername(String username) throws IllegalArgumentException {
        return getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Places the starter on the specified side.
     * @param username of the player
     * @param side of the starter
     * @throws InvalidPlayerActionException if the player cannot perform the operation. For example the player has already chosen the side.
     */
    public void placeStarter(String username, Side side) throws InvalidPlayerActionException {
        try {
            gameState.placeStarter(this, getUserByUsername(username), side);
        } catch (Playground.UnavailablePositionException | Playground.NotEnoughResourcesException ignored) {
            // the placement of the starter cannot cause problems
        }
    }

    /**
     * Places the secret objective from one of the two available.
     * @param username of the player.
     * @param chosenObjective the chosen objective.
     * @throws InvalidPlayerActionException if the player cannot perform the operation. For example the player has already chosen the objective.
     */
    public void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException {
        gameState.placeObjectiveCard(this, getUserByUsername(username), chosenObjective);
    }

    /**
     * Places the card on the side and position specified.
     * @param username of the player.
     * @param card to place.
     * @param side of the card.
     * @param position in the playground.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws Playground.UnavailablePositionException if the position is not available. For example the player is trying to place the card in an already covered corner.
     * @throws Playground.NotEnoughResourcesException if the player's resource are not enough to place the card.
     */
    public void placeCard(String username, Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        gameState.placeCard(this, getUserByUsername(username), card, side, position);
    }

    /**
     * Draws from the resource cards deck
     * @param username of the player
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException if the deck is empty.
     */
    public void drawFromResourceDeck(String username) throws InvalidPlayerActionException, EmptyDeckException {
        gameState.drawFromResourceDeck(this, getUserByUsername(username));
    }

    /**
     * Draws from the golden cards deck
     * @param username of the player
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException if the deck is empty.
     */
    public void drawFromGoldenDeck(String username) throws InvalidPlayerActionException, EmptyDeckException {
        gameState.drawFromGoldenDeck(this, getUserByUsername(username));
    }

    /**
     * Draws from one of the available face up cards.
     * @param username of the player.
     * @param faceUpCardIdx specifying the face up card.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    public void drawFromFaceUpCards(String username, int faceUpCardIdx) throws InvalidPlayerActionException {
        gameState.drawFromFaceUpCards(this, getUserByUsername(username), faceUpCardIdx);
    }

    /**
     * Draws automatically to complete the current player's turn.
     * It may happen if the current player disconnects after placing a card but before drawing the new one.
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

}

