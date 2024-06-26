package it.polimi.ingsw.model;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.jsondeserializer.DeserializationHandler;
import it.polimi.ingsw.model.Deck.Deck;
import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.ChatDatabase;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.model.listenerhandler.ListenerHandler;
import it.polimi.ingsw.model.loader.CardsLoader;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.gamePhase.PhaseHandler;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;

import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Math.min;

/**
 * Representation of the game.
 * Each player is recognised through the system by their username.
 */
public class Game {
    private Deck<Card> resourceCards;
    private Deck<Card> goldenCards;
    private Deck<Card> starterCards;
    private Deck<ObjectiveCard> objectiveCards;

    private final List<String> validUsernames;

    Set<PlayerColor> availableColors;

    private List<Card> faceUpCards;

    private List<ObjectiveCard> commonObjects;

    private static final int MAX_POINT = 20;

    private int currentPlayerIdx; // index in the current player list.

    private final boolean isFinished;

    private boolean isActive;

    private List<Player> players;

    private GamePhase phase;

    private PhaseHandler phaseHandler;

    public static final int MAX_DELAY_FOR_SUSPENDED_GAME = 120000;

    private ListenerHandler<GameListener> listenerHandler;

    // Advanced Features

    // chat database containing the history of all sent messages
    private ChatDatabase chatDatabase;

    private List<Card> createCardList(List<Front> fronts, List<Back> backs) {
        assert fronts.size() == backs.size();

        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < fronts.size(); ++i) {
            cards.add(new Card(fronts.get(i), backs.get(i)));
        }

        return cards;
    }

    private List<ObjectiveCard> createObjectiveCardList(List<ObjectivePositionCard> objectivePositions, List<ObjectiveResourceCard> objectiveResources) {
        List<ObjectiveCard> objectiveCards = new ArrayList<>();

        objectiveCards.addAll(objectivePositions);

        objectiveCards.addAll(objectiveResources);

        return objectiveCards;
    }

    private List<Front> frontFromGoldenList(String goldenFrontCardsPath) throws FileNotFoundException {
        List<GoldenFront> gFronts =
                new DeserializationHandler<GoldenFront>().jsonToList(goldenFrontCardsPath, new TypeToken<>() {
                });
        return new ArrayList<>(gFronts);
    }

    private void loadAvailableColors() {
        this.availableColors = new HashSet<>(
                Arrays.asList(
                        PlayerColor.RED,
                        PlayerColor.BLUE,
                        PlayerColor.GREEN,
                        PlayerColor.YELLOW
                )
        );
    }

    /**
     * Load all kind of cards to the game.
     */
    private void loadCards() {
        this.resourceCards = new Deck<>(CardsLoader.getResourceCards());

        this.goldenCards = new Deck<>(CardsLoader.getGoldenCards());

        this.starterCards = new Deck<>(CardsLoader.getStarterCards());

        this.objectiveCards = new Deck<>(CardsLoader.getObjectiveCards());
    }

    /**
     * Creates a player by assigning him/her a starter card, common objectives cards and three cards to his/her hand.
     *
     * @param username of the player.
     * @return the created player
     * @throws EmptyDeckException if the deck is empty.
     */
    private Player createPlayer(String username) throws EmptyDeckException {
        Card startingCard = starterCards.draw();

        List<Card> userHand = new ArrayList<>();
        userHand.add(resourceCards.draw());
        userHand.add(resourceCards.draw());
        userHand.add(goldenCards.draw());

        List<ObjectiveCard> userObjectives = new ArrayList<>();
        userObjectives.add(objectiveCards.draw());
        userObjectives.add(objectiveCards.draw());
        return new Player(username, startingCard, userHand, userObjectives);
    }

    private Deck<Card> getAssociatedToFaceUpCard(int faceUpIdx) {
        if (faceUpIdx <= 1) {
            return resourceCards;
        } else {
            return goldenCards;
        }
    }

    /**
     * Creates game based on the lobby
     */
    public Game(List<String> validUsernames) {
        loadAvailableColors();
        loadCards();
        try {
            players = new ArrayList<>();
            for (String username : validUsernames) {
                players.add(createPlayer(username));
            }
            Collections.shuffle(players);

            faceUpCards = new ArrayList<>();
            for (int i = 0; i < 4; ++i) {
                faceUpCards.add(getAssociatedToFaceUpCard(i).draw());
            }

            commonObjects = new ArrayList<>();
            for (int i = 0; i < 2; ++i) {
                commonObjects.add(objectiveCards.draw());
            }
        } catch (EmptyDeckException e) {
            // there must be enough cards for the beginning
            e.printStackTrace();
        }

        this.validUsernames = validUsernames;
        phase = GamePhase.Setup;
        currentPlayerIdx = 0;
        isFinished = false;
        isActive = true;
        chatDatabase = new ChatDatabase();
        phaseHandler = new PhaseHandler(validUsernames.size());
        listenerHandler = new ListenerHandler<>();
    }

    // methods

    /**
     * Verifies if the given <code>idx</code> corresponds to a valid player index.
     *
     * @param idx index of the player to be verified.
     * @return true if the <code>idx</code> is valid, false otherwise.
     */
    private boolean isValidFaceUpCardsIdx(int idx) {
        return 0 <= idx && idx < faceUpCards.size();
    }

    /**
     * Finds the next valid current player index, that is, the first player still connected.
     */
    private void updateCurrentPlayerIdx() {
        currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
        Player currentPlayer = players.get(currentPlayerIdx);
        while (!currentPlayer.isConnected()) {
            simulateTurn(currentPlayer.getUsername());
            currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
            currentPlayer = players.get(currentPlayerIdx);
        }
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

        this.currentPlayerIdx = gameBeforeCrash.currentPlayerIdx;
        this.isFinished = gameBeforeCrash.isFinished;
        this.isActive = gameBeforeCrash.isActive;
        this.players = gameBeforeCrash.players;
        this.phase = gameBeforeCrash.phase;
        this.listenerHandler = gameBeforeCrash.listenerHandler;
        this.validUsernames = gameBeforeCrash.validUsernames;
        this.availableColors = gameBeforeCrash.availableColors;
        this.phaseHandler = gameBeforeCrash.phaseHandler;
        this.chatDatabase = gameBeforeCrash.chatDatabase;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIdx);
    }

    public GamePhase getPhase() {
        return phase;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isFinished() {
        return phase == GamePhase.End;
    }

    public List<ObjectiveCard> getCommonObjectives() {
        return commonObjects;
    }

    public List<Card> getFaceUpCards() {
        return faceUpCards;
    }

    public int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public List<Message> getMessages() {
        return chatDatabase.getMessages();
    }

    public Set<PlayerColor> getAvailableColor() {
        return availableColors;
    }

    /**
     * Adds a <code>client</code> to the game.
     *
     * @param username of the player.
     * @param client   to add.
     * @throws InvalidUsernameException if the game has started but the <code>username</code> wasn't registered or if it
     *                                  is already connected in the game.
     */
    public void add(String username, GameListener client) throws InvalidUsernameException {
        // only previously connected users can join the game
        if (!validUsernames.contains(username)) {
            throw new InvalidUsernameException("The game is already started and there are no players registered with the username " + username);
        }

        if (fromUsernameToPlayer(username).isConnected()) {
            throw new InvalidUsernameException("It seems you're already connected");
        }
        System.out.println("Username " + username + " has joined the game");
        listenerHandler.add(username, client);

        setNetworkStatus(username, true);

        listenerHandler.notify(username, receiver -> receiver.resultOfLogin(true,""));

        //   System.out.println("Notify the game representation.\tIsActive = " + isActive);
        ClientGame clientRepresentationOfTheGame = new ClientGame(this);
        listenerHandler.notify(username, receiver -> receiver.updateAfterConnection(clientRepresentationOfTheGame));

        if (!isActive && getListOfActivePlayers().size() > 1) {
            System.out.println("Game is active after being suspended");
            listenerHandler.notifyBroadcast(GameListener::showUpdateGameState);
            isActive = true;
        }

        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdatePlayerStatus(true, username));
    }

    /**
     * Removes a <code>username</code> from the game and suspends the game if the number of players is less than two.
     *
     * @param username of the player to remove from the player.
     * @throws InvalidUsernameException if the username isn't valid.
     */
    public void remove(String username) throws InvalidUsernameException {
        if (!validUsernames.contains(username)) {
            throw new InvalidUsernameException();
        }
        System.out.println("User " + username + " has left the game");
        listenerHandler.remove(username);
        setNetworkStatus(username, false);
        // System.out.println("Set the player " + username + "'s network status to false");
        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdatePlayerStatus(false, username));
        if (getListOfActivePlayers().size() < 2) {
            System.err.println("Game is suspended");
            listenerHandler.notifyBroadcast(GameListener::showUpdateGameState);
            isActive = false;
        }
    }

    private void setNetworkStatus(String username, boolean networkStatus) {
        players.stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .ifPresent(p -> p.setNetworkStatus(networkStatus));
    }

    /**
     * Returns the corresponding player, given the <code>username</code>.
     *
     * @param username of the player to be returned.
     * @return the player that matches the <code>username</code>.
     */
    private Player fromUsernameToPlayer(String username) {
        return players.stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .get();
    }

    /**
     * Returns a player based on the username.
     *
     * @param username of the player.
     * @return the player.
     */
    public Player getPlayerByUsername(String username) {
        return fromUsernameToPlayer(username);
    }

    /**
     * Places the starter on the specified side.
     *
     * @param username of the player
     * @param side     of the starter
     * @throws InvalidPlayerActionException if the player cannot perform the operation. For example the player has already chosen the side.
     */
    public void placeStarter(String username, Side side)
            throws InvalidPlayerActionException,
            InvalidGamePhaseException {
        if (phase != GamePhase.Setup) {
            throw new InvalidGamePhaseException();
        }
        Player player = getPlayerByUsername(username);

        try {
            player.placeStarter(side);

            Position starterPosition = new Position(0, 0);
            Playground playground = player.getPlayground();
            Map<Position, CornerPosition> cornersBeingCovered = new HashMap<>(playground.getCornersBeingCoveredByTheTileAt(starterPosition));
            List<Position> availablePositions = new ArrayList<>(playground.getAvailablePositions());
            Map<Symbol, Integer> resources = new HashMap<>(playground.getResources());
            Card starter = player.getStarter();
            listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateAfterPlace(
                    cornersBeingCovered,
                    availablePositions,
                    resources,
                    0,
                    username,
                    new ClientCard(starter),
                    side,
                    starterPosition
            ));
        } catch (Playground.UnavailablePositionException | Playground.NotEnoughResourcesException e) {
            System.err.println("Error: the placement of the starter should not cause any exception related to the Playground, unless it's an illegal call");
            // the starter card shouldn't cause any exception related to the playground
            e.printStackTrace();
        }
    }

    /**
     * Assigns at <code>username</code> the specified <code>color</code>.
     *
     * @param username of the player who has chose the color.
     * @param color    chosen.
     * @throws InvalidPlayerActionException if the player cannot perform this action.
     * @throws InvalidColorException        if the color has already been selected by others.
     * @throws NonexistentPlayerException   if the username is invalid.
     * @throws InvalidGamePhaseException    if the player has already finished their setup.
     */
    public void assignColor(String username, PlayerColor color) throws InvalidPlayerActionException, InvalidColorException, NonexistentPlayerException, InvalidGamePhaseException {
        if (phase != GamePhase.Setup) {
            throw new InvalidGamePhaseException();
        }

        if (!availableColors.contains(color)) {
            throw new InvalidColorException();
        }

        Player player = getPlayerByUsername(username);
        player.assignColor(color);
        availableColors.remove(color);
        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateColor(color, username));
    }

    /**
     * Places the secret objective from one of the two available.
     *
     * @param username        of the player.
     * @param chosenObjective the chosen objective. 0 for the first one, 1 for the second one.
     * @throws InvalidPlayerActionException if the player cannot perform the operation. For example the player has already chosen the objective.
     * @throws InvalidGamePhaseException    if the player has already finished the setup.
     */
    public void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException, InvalidGamePhaseException {
        if (chosenObjective != 0 && chosenObjective != 1) {
            throw new IllegalArgumentException();
        }
        if (phase != GamePhase.Setup) {
            throw new InvalidGamePhaseException();
        }

        Player player = getPlayerByUsername(username);

        player.placeObjectiveCard(chosenObjective);

        ObjectiveCard secretObjective = player.getObjective();
        listenerHandler.notify(username, receiver -> receiver.showUpdateObjectiveCard(new ClientObjectiveCard(secretObjective), username));

        phase = phaseHandler.getNextPhase(phase, currentPlayerIdx);
        if (phase == GamePhase.PlaceNormal) {
            // the current player maybe inactive when the setup finishes. If the game is active a new current player must be chosen
            if (!players.get(currentPlayerIdx).isConnected() && isActive) {
                updateCurrentPlayerIdx();
            }
            GamePhase currPhase = phase;
            listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateCurrentPlayer(currentPlayerIdx, currPhase));
        }
    }

    /**
     * Places the card on the side and position specified.
     *
     * @param username of the player.
     * @param card     to place.
     * @param side     of the card.
     * @param position in the playground
     * @throws InvalidPlayerActionException            if the player cannot perform the operation.
     * @throws Playground.UnavailablePositionException if the position is not available. For example the player is trying to place the card in an already covered corner.
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place the card.
     * @throws InvalidGamePhaseException               if the game phase cannot allow placing cards.
     */
    public void placeCard(String username, Card card, Side side, Position position)
            throws InvalidPlayerActionException,
            Playground.UnavailablePositionException,
            Playground.NotEnoughResourcesException,
            InvalidGamePhaseException, SuspendedGameException {
        if (!isActive) {
            throw new SuspendedGameException();
        }
        if (phase != GamePhase.PlaceNormal && phase != GamePhase.PlaceAdditional) {
            throw new InvalidGamePhaseException();
        }
        Player currentPlayer = getPlayerByUsername(username);
        if (!currentPlayer.getUsername().equals(username)) {
            throw new InvalidPlayerActionException();
        }

        int newScore = currentPlayer.placeCard(card, side, position);

        if (newScore >= MAX_POINT) {
            phaseHandler.setLastNormalTurn();
        }

        phase = phaseHandler.getNextPhase(phase, currentPlayerIdx);

        Playground playground = currentPlayer.getPlayground();
        Map<Position, CornerPosition> cornersBeingCovered = new HashMap<>(playground.getCornersBeingCoveredByTheTileAt(position));
        List<Position> availablePositions = new ArrayList<>(playground.getAvailablePositions());
        Map<Symbol, Integer> resources = new HashMap<>(playground.getResources());
        int score = currentPlayer.getPoints();

        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateAfterPlace(
                cornersBeingCovered,
                availablePositions,
                resources,
                score,
                username,
                new ClientCard(card),
                side,
                position
        ));

        if (phase == GamePhase.PlaceAdditional) {
            updateCurrentPlayerIdx();
            GamePhase currPhase = phase;
            listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateCurrentPlayer(currentPlayerIdx, currPhase));
        }

        if (phase == GamePhase.End) {
            List<String> winners = getWinners();
            listenerHandler.notifyBroadcast(receiver -> receiver.showWinners(winners));
        }
    }

    /**
     * Draws a card from the deck specified.
     *
     * @param username of the player to draw.
     * @param deckType of the deck to draw a card from.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException           if the selected deck is empty.
     * @throws InvalidGamePhaseException    if the game phase doesn't allow the operation.
     */
    public void drawFromDeck(String username, DeckType deckType) throws InvalidPlayerActionException, EmptyDeckException, InvalidGamePhaseException {
        if (!phase.equals(GamePhase.DrawNormal)) {
            throw new InvalidGamePhaseException();
        }

        Player currentPlayer = players.get(currentPlayerIdx);

        if (!currentPlayer.getUsername().equals(username)) {
            throw new InvalidPlayerActionException();
        }

        Deck<Card> deck;
        if (deckType.equals(DeckType.GOLDEN))
            deck = goldenCards;
        else
            deck = resourceCards;

        Card newCard = deck.draw();

        try {
            currentPlayer.addCard(newCard);
            if (goldenCards.isEmpty() && resourceCards.isEmpty()) {
                phaseHandler.setLastNormalTurn();
            }
            phase = phaseHandler.getNextPhase(phase, currentPlayerIdx);

            updateCurrentPlayerIdx();

        } catch (InvalidPlayerActionException invalidPlayerActionException) {
            deck.add(newCard);
            throw invalidPlayerActionException;
        }

        Card top = deck.getTop();
        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateAfterDraw(
                new ClientCard(newCard),
                top == null ? null : new ClientFace(top.getFace(Side.BACK)),
                null,
                username,
                convertDeckTypeIntoId(deckType)));
        GamePhase currPhase = phase;
        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateCurrentPlayer(currentPlayerIdx, currPhase));
    }

    /**
     * Fills the missing spot on faceUpCards
     *
     * @param faceUpCardIdx the spot of the taken card
     * @return the deck from which the face up card has been replaced
     */
    private Deck<Card> replaceFaceUpCard(int faceUpCardIdx) {
        Card replacement = null;
        Deck<Card> deckForReplacement = null;
        try {
            deckForReplacement = getAssociatedToFaceUpCard(faceUpCardIdx);
            replacement = deckForReplacement.draw();
        } catch (EmptyDeckException e) {
            // test the other deck
            try {
                deckForReplacement = getAssociatedToFaceUpCard((faceUpCardIdx + 2) % faceUpCards.size());
                replacement = deckForReplacement.draw();
            } catch (EmptyDeckException otherDeckEmptyException) {
                phaseHandler.setLastNormalTurn();
            }
        }

        faceUpCards.set(faceUpCardIdx, replacement); // null if there's none card for replacement

        return deckForReplacement;
    }

    /**
     * Draws from one of the available face up cards.
     *
     * @param username      of the player.
     * @param faceUpCardIdx specifying the face up card.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    public void drawFromFaceUpCards(String username, int faceUpCardIdx) throws InvalidPlayerActionException, InvalidGamePhaseException, InvalidFaceUpCardException {
        if (!phase.equals(GamePhase.DrawNormal)) {
            throw new InvalidGamePhaseException();
        }

        Player currentPlayer = getPlayerByUsername(username);

        if (!currentPlayer.getUsername().equals(username)) {
            throw new InvalidPlayerActionException();
        }

        assert (isValidFaceUpCardsIdx(faceUpCardIdx));

        if (faceUpCards.get(faceUpCardIdx) == null) {
            throw new InvalidFaceUpCardException("No face up card there");
        }

        Card newCard = faceUpCards.get(faceUpCardIdx);

        Deck<Card> deckForReplacement;
        try {
            currentPlayer.addCard(newCard);
            deckForReplacement = replaceFaceUpCard(faceUpCardIdx);
            phase = phaseHandler.getNextPhase(phase, currentPlayerIdx);
            updateCurrentPlayerIdx();
        } catch (InvalidPlayerActionException e) {
            throw new InvalidPlayerActionException();
        }

        Card top = deckForReplacement.getTop();
        Card faceUpCard = faceUpCards.get(faceUpCardIdx);
        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateAfterDraw(
                new ClientCard(newCard),
                top == null ? null : new ClientFace(top.getFace(Side.BACK)),
                faceUpCard == null ? null : new ClientCard(faceUpCard),
                username, faceUpCardIdx));
        GamePhase currPhase = phase;
        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateCurrentPlayer(currentPlayerIdx, currPhase));
    }

    /**
     * Simulates the <code>current player</code>'s placement and card draw in a normal or additional placing phase.
     *
     * @param currentPlayer whose turn is to be simulated.
     */
    private void simulateTurn(String currentPlayer) {
        if (phase != GamePhase.PlaceNormal && phase != GamePhase.PlaceAdditional) {
            System.err.println("Error: " + "A turn can be skipped only at the beginning");
            throw new RuntimeException("A turn can be skipped only at the beginning");
        }
        if (!currentPlayer.equals(players.get(currentPlayerIdx).getUsername())) {
            System.err.println("Error: " + "Only the current player can skip their turn");
            throw new RuntimeException("Only the current player can skip their turn");
        }

        if (phase == GamePhase.PlaceNormal) {
            // additional getNextPhase is required because the player has to skip the placement and the draw
            phase = phaseHandler.getNextPhase(phase, currentPlayerIdx);
        }

        phase = phaseHandler.getNextPhase(phase, currentPlayerIdx);
    }

    /**
     * Skips the current player's turn.
     * The method is invoked whenever the current player is not active.
     */
    public void skipTurn(String currentPlayer) {
        simulateTurn(currentPlayer);

        if (phase == GamePhase.PlaceAdditional || phase == GamePhase.PlaceNormal) {
            updateCurrentPlayerIdx();
            GamePhase currPhase = phase;
            listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateCurrentPlayer(currentPlayerIdx, currPhase));
        }

        if (phase == GamePhase.End) {
            List<String> winners = getWinners();
            listenerHandler.notifyBroadcast(receiver -> receiver.showWinners(winners));
        }
    }

    /**
     * Registers message sent by author.
     *
     * @param message to register.
     * @throws InvalidMessageException if the author doesn't match the sender or the recipient is an invalid username.
     */
    public void registerMessage(Message message) throws InvalidMessageException {
        if (!message.getRecipient().equals("Everyone") && getPlayerByUsername(message.getRecipient()) == null) {
            throw new InvalidMessageException("recipient doesn't exists");
        }
        chatDatabase.addMessage(message);
        listenerHandler.notifyBroadcast(receiver -> receiver.showUpdateChat(message));
    }

    private List<Player> getListOfActivePlayers() {
        return players.stream()
                .filter(Player::isConnected)
                .toList();
    }

    /**
     * Returns a list of active players.
     *
     * @return an active player's list.
     */
    public List<Player> getActivePlayers() {
        return getListOfActivePlayers();
    }

    /**
     * Gets the card associated to the <code>cardId</code>.
     *
     * @param username the player's username.
     * @param frontId  the id of the card's front.
     * @param backId   the id of the card's back.
     * @return the player's card associated with <code>cardId</code>.
     */
    public Card getCard(String username, int frontId, int backId) throws InvalidCardIdException {
        Player player = getPlayerByUsername(username);
        return player.getCard(frontId, backId);
    }

    /**
     * Returns winners' name.
     *
     * @return the list of players' name winning the game (they can be more than one in case of a tie).
     */
    private List<String> getWinners() {
        if (phase != GamePhase.End) {
            System.err.println("Error: game not finished yet");
        }

        int maxPointNormalTurns = 29;
        int maxPoints = -1;
        List<Player> winners = new ArrayList<>();

        for (Player player : players) {
            int finalPoints = min(maxPointNormalTurns, player.getPoints()) + player.calculateExtraPoints(commonObjects);
            boolean win = false;
            boolean tie = false;

            if (finalPoints > maxPoints) {
                win = true;
            } else if (finalPoints == maxPoints) {
                int winnerSatisfiedObjectives = winners.getFirst().getNumSatisfiedObjectives();
                int playerObjectives = player.getNumSatisfiedObjectives();

                if (winnerSatisfiedObjectives < playerObjectives) {
                    win = true;
                } else if (winnerSatisfiedObjectives == playerObjectives) {
                    tie = true;
                }
            }

            if (win) {
                maxPoints = finalPoints;
                winners.clear();
                winners.add(player);
            }
            if (tie) {
                winners.add(player);
            }
        }
        return winners.stream().map(Player::getUsername).toList();
    }

    public void terminateForInactivity() {
        try {
            String lastConnectedPlayer = getListOfActivePlayers().getFirst().getUsername();
            phase = GamePhase.End;
            listenerHandler.notifyBroadcast(receiver -> receiver.showWinners(Collections.singletonList(lastConnectedPlayer)));
        } catch (NoSuchElementException noSuchElementException) {
            // empty list: there's no player to notify
        }
    }

    public Face getTopDeckBack(DeckType type) {
        if (type == DeckType.GOLDEN) {
            return goldenCards.getTop().getFace(Side.BACK);
        }
        return resourceCards.getTop().getFace(Side.BACK);
    }

    /**
     * Converts the deck <code>type</code> to its respective id number.
     *
     * @param type of deck provide.
     * @return four if the deck <code>type</code> is golden, five if the deck <code>type</code> is resource.
     */
    private int convertDeckTypeIntoId(DeckType type) {
        if (DeckType.GOLDEN == type) {
            return 4;
        } else {
            return 5;
        }
    }
}

