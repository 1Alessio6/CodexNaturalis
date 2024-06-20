package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.controller.InvalidIdForDrawingException;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.card.NotExistingFaceUp;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.client.view.tui.drawplayground.*;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Client TUI represents the client that used a Text-based user interface
 */
public class ClientTUI implements View {
    private final Scanner console;
    private final Set<TUIActions> availableActions = new HashSet<>();

    private Side cardSide = Side.FRONT;

    private final ClientController controller;

    /* saves the coordinate to start print */
    private Position currOffset = new Position(0, 0);

    /* saves the current watching player, so updates are correctly applied */
    private String currentWatchingPlayer;

    /**
     * Constructs clientTUI with the <code>controller</code> provided.
     *
     * @param controller the representation of the client controller.
     */
    public ClientTUI(ClientController controller) {
        this.controller = controller;
        this.console = new Scanner(System.in);

        setActionsForConnection();
    }

    private void setActionsForConnection() {
        availableActions.add(TUIActions.HELP);
        availableActions.add(TUIActions.QUIT);
        availableActions.add(TUIActions.CONNECT);
    }

    /**
     * Parses the player's commands.
     */
    private void parseGameCommands() {
        // TODO: command condition should be changed
        while (console.hasNextLine()) {
            // split command with spaces and analyze the first word
            String[] nextCommand = console.nextLine().toLowerCase().split(" ", 3);

            try {
                if (availableActions.contains(TUIActions.valueOf(nextCommand[0].toUpperCase()))) {
                    // before running command
                    ClientUtil.clearExceptionSpace();

                    switch (TUIActions.valueOf(nextCommand[0].toUpperCase())) {
                        case CONNECT -> connect(nextCommand[1], Integer.parseInt(nextCommand[2]));
                        case SELECTUSERNAME -> selectUsername(nextCommand[1]);
                        case BACK -> goBack();
                        case COLOR -> chooseColor();
                        case FLIP -> flip();
                        case HELP -> ClientUtil.printHelpCommands(availableActions);
                        case SPY -> spy(nextCommand[1]);
                        case M, PM -> sendMessage(nextCommand);
                        case DRAW -> draw();
                        case PLACE -> place();
                        case QUIT -> quit();
                        case LOBBYSIZE ->
                                setupLobbyPlayerNumber(Integer.parseInt(nextCommand.length == 2 ? nextCommand[1] : "0")); //todo: could be done in a better way?
                        case OBJECTIVE -> chooseObjective();
                        case STARTER -> placeStarter();
                        case RULEBOOK -> displayRulebook();
                        case MVPG -> movePlayground(nextCommand[1]);
                    }
                } else {
                    ClientUtil.printExceptions(ExceptionsTUI.INVALID_GAME_COMMAND.getMessage());
                    // print help for consented commands
                    synchronized (availableActions){
                        ClientUtil.printHelpCommands(availableActions);
                    }
                }
            } catch (UndrawablePlaygroundException |
                     InvalidPlayersNumberException |
                     InvalidMessageException | InvalidIdForDrawingException | EmptyDeckException |
                     InvalidColorException | NotExistingFaceUp | Playground.UnavailablePositionException |
                     Playground.NotEnoughResourcesException | InvalidGamePhaseException | SuspendedGameException | TUIException e) {
                ClientUtil.printExceptions(e.getMessage());
                // print help for consented commands
                synchronized (availableActions){
                    ClientUtil.printHelpCommands(availableActions);
                }
            } catch (IllegalArgumentException e) {

                ClientUtil.printExceptions(ExceptionsTUI.INVALID_GAME_COMMAND.getMessage());
                // print help for consented commands
                synchronized (availableActions){
                    ClientUtil.printHelpCommands(availableActions);
                }
            } catch (IndexOutOfBoundsException e) {
                ClientUtil.printExceptions("few arguments");
                //ClientUtil.printExceptions(ExceptionsTUI.INVALID_SPY_INPUT.getMessage());
                // print help for consented commands
                ClientUtil.printHelpCommands(availableActions);
            } catch (UnReachableServerException e) {
                ClientUtil.printExceptions("Server is down, please connect to another server");
                availableActions.clear();
                setActionsForConnection();
            } finally {
                ClientUtil.putCursorToInputArea();
            }
        }
    }

    private Position stringToPos(String string) {
        String[] myPos = string.split(",", 2);
        int x = Integer.parseInt(myPos[0].trim());
        int y = Integer.parseInt(myPos[1].trim());

        return new Position(x, y);
    }

    /**
     * Method used to move the playground to a position (classic cartesian system)
     *
     * @param requestedOffsetString is the argument of the move command
     */
    private void movePlayground(String requestedOffsetString)
            throws UndrawablePlaygroundException {
        Position requestedOffset = stringToPos(requestedOffsetString);

        currOffset = ClientUtil.printPlayground(controller.getPlaygroundByUsername(currentWatchingPlayer),
                currOffset, requestedOffset);

        // clear input area
        ClientUtil.printCommandSquare();
    }

    /**
     * Restores the current playing area of the player.
     */
    private void goBack() throws UnInitializedPlaygroundException, FittablePlaygroundException,
                                 InvalidCardRepresentationException, InvalidCardDimensionException {
        // so updates arrive correctly to main player
        currentWatchingPlayer = controller.getMainPlayerUsername();
        // add back commands
        setAvailableActions();
        // print main player stuff again
        showUpdateAfterConnection();
        // clear input area
        ClientUtil.printCommandSquare();
    }

    /**
     * Flips the cards over.
     */
    private void flip() {
        // invert side
        cardSide = cardSide.equals(Side.FRONT) ? Side.BACK : Side.FRONT;

        List<ClientCard> toPrint = (controller.isMainPlaygroundEmpty()) ?
                Collections.singletonList(controller.getMainPlayerStarter()) : controller.getMainPlayerCards();

        ClientUtil.printPlayerHand(toPrint, cardSide);

        // clear input area
        ClientUtil.printCommandSquare();
    }

    /**
     * Quits the game.
     */
    private void quit() {
        controller.disconnect(controller.getMainPlayerUsername());

        ClientUtil.clearScreen();
        System.exit(0);
    }

    /**
     * Receives the index of the secret objective card chosen by the player.
     *
     * @throws InvalidGamePhaseException if the game phase doesn't allow choosing objective cards.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws TUIException              if the player enters an invalid objective index.
     */
    private void chooseObjective() throws InvalidGamePhaseException, SuspendedGameException, TUIException {
        try {
            ClientUtil.printCommand("Choose objective idx: ");
            int objectiveIdx = Integer.parseInt(console.nextLine()); // starting from 1

            if (objectiveIdx == 1 || objectiveIdx == 2) {
                controller.placeObjectiveCard(objectiveIdx - 1);
            } else throw new TUIException(ExceptionsTUI.INVALID_IDX);

            // clear input area
            ClientUtil.printCommandSquare();
            //ClientUtil.putCursorToInputArea();
        } catch (NumberFormatException e) {
            throw new TUIException(ExceptionsTUI.INVALID_IDX);
        }
    }

    /**
     * Sets the number of players allowed in the lobby.
     *
     * @param size of the lobby.
     * @throws InvalidPlayersNumberException if the <code>size</code> is greater than 4 or less than 2
     * @throws NumberFormatException         if the player digits an entry that isn't a number.
     */
    private void setupLobbyPlayerNumber(int size) throws InvalidPlayersNumberException, NumberFormatException {
        controller.setPlayersNumber(size);
        // remove manually: only creator has this command
        availableActions.remove(TUIActions.LOBBYSIZE);

        //ClientUtil.putCursorToInputArea();
    }

    /**
     * Receives the color chosen by the player.
     *
     * @throws InvalidColorException     if the color has already been selected by others.
     * @throws InvalidGamePhaseException if the game phase doesn't allow choosing colors.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws TUIException              if the player enters an invalid color.
     */
    private void chooseColor() throws InvalidColorException, InvalidGamePhaseException, SuspendedGameException, IllegalArgumentException, TUIException {
        ClientUtil.printCommand("Choose color: ");

        try {
            PlayerColor color = PlayerColor.valueOf(console.nextLine().toUpperCase());
            controller.chooseColor(color);
        } catch (IllegalArgumentException e) {//catch (IllegalArgumentException |InvalidColorException e){
            throw new TUIException(ExceptionsTUI.INVALID_COLOR);
        }

        // clear input area
        ClientUtil.printCommandSquare();
        //ClientUtil.putCursorToInputArea();
    }

    /**
     * Sends a message.
     *
     * @param command an array of strings containing the message and the form in which the message is to be transmitted.
     * @throws InvalidMessageException if the author doesn't match the author or the recipient doesn't exist.
     * @throws TUIException            if the player attempts to send a message incorrectly, that means, not following
     *                                 the sending message structure.
     */
    private void sendMessage(String[] command) throws InvalidMessageException, TUIException {
        try {
            String commandContent = command[1];
            Message myMessage = command[0].equals("pm") ? createPrivateMessage(commandContent) : createBroadcastMessage(commandContent);
            controller.sendMessage(myMessage);

            // clear input area
            ClientUtil.printCommandSquare();
            //ClientUtil.putCursorToInputArea();
        } catch (IndexOutOfBoundsException e) {
            throw new TUIException(ExceptionsTUI.INVALID_MESSAGE_INPUT);
        }
    }

    /**
     * Creates a private message
     *
     * @param messageDetails the recipient and the content of the message.
     * @return a message with an author, a recipient and the content of the message.
     */
    private Message createPrivateMessage(String messageDetails) throws InvalidMessageException {
        // messageDetails contains recipient too
        String[] messageSplit = messageDetails.split(" ", 2);
        String recipient = messageSplit[0];
        String messageContent = messageSplit[1];
        return new Message(controller.getMainPlayerUsername(), recipient, messageContent);
    }

    /**
     * Creates a message in broadcast.
     *
     * @param messageContent the content of the message.
     * @return a message with the author and the content of the message.
     */
    private Message createBroadcastMessage(String messageContent) throws InvalidMessageException {
        return new Message(controller.getMainPlayerUsername(), messageContent);
    }

    /**
     * Receives the position of the card to draw.
     *
     * @throws InvalidIdForDrawingException if the id isn't valid for drawing.
     * @throws EmptyDeckException           in the event that the deck is empty.
     * @throws NotExistingFaceUp            if the face up slot is empty.
     * @throws InvalidGamePhaseException    if the game doesn't allow to draw cards.
     * @throws SuspendedGameException       if the game is suspended.
     * @throws TUIException                 if the player inserts an inappropriate argument.
     */
    private void draw() throws InvalidIdForDrawingException, EmptyDeckException, NotExistingFaceUp, InvalidGamePhaseException, SuspendedGameException, TUIException {
        //ClientUtil.printCommand("Insert the position of the card you want to draw: ");
        ////int drawFromId = Integer.parseInt(console.nextLine()) - 1;
        //controller.draw(drawFromId);
        try {
            ClientUtil.printCommand("Insert the position of the card you want to draw: ");
            int drawFromId = Integer.parseInt(console.nextLine()) - 1;
            controller.draw(drawFromId);

            // clear input area
            ClientUtil.printCommandSquare();
            //ClientUtil.putCursorToInputArea();
        } catch (IllegalArgumentException e) {
            throw new TUIException(ExceptionsTUI.INVALID_CARD_POSITION);
        }
    }

    /**
     * Places a specific card on a specific side at a specific position of the playground.
     *
     * @throws Playground.UnavailablePositionException if the position isn't available or if it is already occupied.
     * @throws Playground.NotEnoughResourcesException  if the needed resources to place the card aren't enough.
     * @throws InvalidGamePhaseException               if the game phase doesn't allow placing cards.
     * @throws SuspendedGameException                  if the game is suspended.
     * @throws TUIException                            if the player enters an invalid side or position to place in.
     */
    private void place() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, TUIException {
        int handPos = receivePlayerCardPosition();
        Side cardSide = receiveSide();
        Position newCardPos = receivePosition();
        controller.placeCard(handPos, cardSide, newCardPos);
    }

    /**
     * Places the starter card on the given side.
     *
     * @throws InvalidGamePhaseException if the game doesn't allow placing starter cards.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws TUIException              if the player enters an invalid card side.
     */
    private void placeStarter() throws InvalidGamePhaseException, SuspendedGameException, TUIException {
        Side starterSide = receiveSide();
        controller.placeStarter(starterSide);

        // clear input area
        ClientUtil.printCommandSquare();
        //ClientUtil.putCursorToInputArea();
    }

    /**
     * Receives the side of the card that is to be placed.
     *
     * @return the side of the card.
     * @throws TUIException if the player enters an invalid card side.
     */
    private Side receiveSide() throws TUIException {
        try {
            ClientUtil.printCommand("What side of the card you want to place, front or back? ");
            return Side.valueOf(console.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TUIException(ExceptionsTUI.INVALID_SIDE);
        }
    }

    /**
     * Receives the position of the card in the player's hand.
     *
     * @return the position of the card if it is valid, a TUIException otherwise.
     * @throws TUIException if the player enters an invalid card position.
     */
    private int receivePlayerCardPosition() throws TUIException {
        ClientUtil.printCommand("Enter card position in your hand");
        try {
            int cardPosition = Integer.parseInt(console.nextLine());

            // ClientUtil.putCursorToInputArea();

            if (cardPosition >= 1 && cardPosition <= 3) return cardPosition - 1;
            else throw new TUIException(ExceptionsTUI.INVALID_CARD_POSITION);
        } catch (InputMismatchException | IllegalArgumentException e) {

            throw new TUIException(ExceptionsTUI.INVALID_CARD_POSITION);
        }

    }

    /**
     * Receives a position of the playground.
     *
     * @return the position.
     * @throws TUIException if the player enters an invalid playground position.
     */
    private Position receivePosition() throws TUIException {
        try {
            ClientUtil.printCommand("Insert position, with x and y, comma separated: ");
            Position requestedPos = stringToPos(console.nextLine());

            // clear input area
            ClientUtil.printCommandSquare();

            return requestedPos;
        } catch (InputMismatchException e) {
            throw new TUIException(ExceptionsTUI.INVALID_CARD_POSITION);
        }
    }

    /**
     * Displays the rulebook on the screen.
     *
     * @throws TUIException if the player enters an invalid page number.
     */
    private void displayRulebook() throws TUIException {
        int numberOfPage = receivePlayerRulebookPage();
        ClientUtil.printRulebook(numberOfPage);
        // remove game actions while reading manual
        availableActions.clear();
        availableActions.add(TUIActions.M);
        availableActions.add(TUIActions.PM);
        availableActions.add(TUIActions.HELP);
        availableActions.add(TUIActions.QUIT);
        availableActions.add(TUIActions.BACK);
        availableActions.add(TUIActions.RULEBOOK);
        //ClientUtil.putCursorToInputArea();
    }

    /**
     * Receives the number of the page to display.
     *
     * @return the number of the page.
     * @throws TUIException if the player enters an invalid number of page.
     */
    private int receivePlayerRulebookPage() throws TUIException {
        ClientUtil.printCommand("Insert page (1 or 2): ");
        int numberOfPage = Integer.parseInt(console.nextLine());

        if (numberOfPage == 1 || numberOfPage == 2) {
            return numberOfPage;
        } else throw new TUIException(ExceptionsTUI.INVALID_PAGE);
    }

    private void connect(String ip, Integer port) throws UnReachableServerException {
        controller.configureClient(this, ip, port);
        availableActions.remove(TUIActions.CONNECT);
        ClientUtil.printCommand("Insert your username: type <selectusername> <your username> (max 15 characters):");
        availableActions.add(TUIActions.SELECTUSERNAME);
    }

    private void selectUsername(String username) {
        if (username.length() > 15) {
            throw new IndexOutOfBoundsException("Username too long");
        }
        this.currentWatchingPlayer = username;
        availableActions.remove(TUIActions.SELECTUSERNAME);
        controller.connect(username);
    }

    /**
     * This method is invoked in a new thread at the beginning of a game
     * Commands can't be interrupted
     */
    @Override
    public void runView() {
        ClientUtil.clearScreen();
        ClientUtil.printCommand("Welcome.\nTo play connect to the server: type connect <ip> <port>");
        // todo: synchronize to have correct command list
        new Thread(this::parseGameCommands).start();
    }

    /**
     * Displays the playground of the <code>playerIdx</code> player.
     *
     * @throws TUIException if the player attempts to spy himself.
     */
    private void spy(String username) throws TUIException, UndrawablePlaygroundException {
        ClientPlayer player = this.controller.getPlayer(username);
        if (player == null)
            throw new TUIException(ExceptionsTUI.INVALID_SPY_INPUT);

        ClientPlayground playground = player.getPlayground();

        if (this.controller.getMainPlayer().equals(player)) {
            throw new TUIException(ExceptionsTUI.INVALID_SPY_COMMAND);
        } else {
            // make showUpdate work on this player
            this.currentWatchingPlayer = username;
            // update commands when you are looking at other players
            availableActions.remove(TUIActions.DRAW);
            availableActions.remove(TUIActions.PLACE);
            availableActions.remove(TUIActions.FLIP);
            availableActions.add(TUIActions.BACK);

            // update only playground, hand and resources
            ClientUtil.printResourcesArea(playground.getResources());
            currOffset = ClientUtil.printPlayground(playground, currOffset);
            ClientUtil.printPlayerHand(controller.getPlayer(username).getPlayerCards(), cardSide);

            //clear input area
            ClientUtil.printCommandSquare();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showServerCrash() {
        ClientUtil.printCommand("Server is crashed. To connect again you have to join the game");
        availableActions.clear();
        setActionsForConnection();
    }

    /**
     * Sets available actions in accordance to the current phase of the game.
     */
    private void setAvailableActions() {
        GamePhase currentPhase = controller.getGamePhase();

        synchronized (availableActions) {
            this.availableActions.clear();
            availableActions.add(TUIActions.HELP);
            availableActions.add(TUIActions.QUIT);

            // add actions only if game is active
            if (controller.isGameActive()) {
                if (currentPhase != null) { // if not in lobby
                    availableActions.add(TUIActions.M);
                    availableActions.add(TUIActions.PM);
                    availableActions.add(TUIActions.FLIP);
                    availableActions.add(TUIActions.RULEBOOK);
                    availableActions.add(TUIActions.MVPG);
                }

                switch (currentPhase) {
                    case Setup -> {
                        // starter command available only if user have to do starter stuff
                        if (controller.isMainPlaygroundEmpty()) {
                            availableActions.add(TUIActions.STARTER);
                        } else if (controller.getMainColor() == null) {
                            availableActions.add(TUIActions.COLOR);
                        } else if (controller.getMainPlayer().getObjectiveCards().size() != 1) {
                            availableActions.add(TUIActions.OBJECTIVE);
                        }
                    }
                    // don't let user use unneeded commands when it's not their turn
                    case DrawNormal -> {
                        if (this.controller.getCurrentPlayerUsername().equals(this.controller.getMainPlayerUsername()))
                            availableActions.add(TUIActions.DRAW);

                        availableActions.add(TUIActions.SPY);
                    }
                    case PlaceNormal, PlaceAdditional -> {
                        if (this.controller.getCurrentPlayerUsername().equals(this.controller.getMainPlayerUsername()))
                            availableActions.add(TUIActions.PLACE);

                        availableActions.add(TUIActions.SPY);
                    }
                    case null -> {}
                    case End -> {}
                }
            }
        }
    }

    // SHOW UPDATE

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby() {
        // print scoreboard (even though there are no points)
        ClientUtil.printWaitingList(controller.getUsernames());

        ClientUtil.putCursorToInputArea();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCreator() {
        ClientUtil.printCommand("""
                Welcome to the new lobby!
                Please set the lobby size (2 to 4 players allowed)
                Type 'lobbysize <number>' to set the lobby size""");

        // add manually: only creator has this command
        availableActions.add(TUIActions.LOBBYSIZE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterLobbyCrash() {
        ClientUtil.printCommand("Lobby crashed! You will be disconnected. Please restart the client...");
        setActionsForConnection();
    }

    @Override
    public void showUpdateFullLobby() {
        ClientUtil.printCommand("Full lobby, sorry you have to connect somewhere else");
        setActionsForConnection();
    }

    @Override
    public void showUpdateExceedingPlayer() {
        ClientUtil.printCommand("You're an exceeding player!");
        setActionsForConnection();
    }

    @Override
    public void showInvalidLogin(String details) {
        ClientUtil.printExceptions("Invalid username! Reason: " + details);
        ClientUtil.printCommand("Please insert a new username with the command <selectusername> <your name>");
        availableActions.add(TUIActions.SELECTUSERNAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterConnection() {
        ClientUtil.clearScreen();

        // set currentWatchingUsername for the first time
        currentWatchingPlayer = controller.getCurrentPlayerUsername();

        ClientPlayground playerPlayground = controller.getMainPlayerPlayground();

        ClientUtil.printScoreboard(this.controller.getPlayers());
        ClientUtil.printResourcesArea(playerPlayground.getResources());
        ClientUtil.printFaceUpCards(this.controller.getFaceUpCards());
        ClientUtil.printCommandSquare();
        ClientUtil.printChatSquare();
        // print decks
        ClientUtil.printDecks(controller.getGoldenDeckTopBack(), controller.getResourceDeckTopBack());

        // print objective(s)
        showUpdateObjectiveCard();

        // when printing for first time,

        try {
            currOffset = ClientUtil.printPlayground(playerPlayground, currOffset);
        } catch (UndrawablePlaygroundException e) {
            ClientUtil.writeLine(GameScreenArea.INPUT_AREA.getScreenPosition().getX() + 11,
                    GameScreenArea.INPUT_AREA.getScreenPosition().getY() + 1,
                    GameScreenArea.INPUT_AREA.getWidth() - 2,
                    e.getMessage());
        }
        // check if there is any occupied tile: it means starter has been placed
        ClientUtil.printPlayerHand(controller.isMainPlaygroundEmpty() ?
                        Collections.singletonList(this.controller.getMainPlayerStarter()) :
                        controller.getMainPlayerCards(),
                cardSide);

        ClientUtil.putCursorToInputArea();


        setAvailableActions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayerStatus() {
        ClientUtil.printScoreboard(this.controller.getPlayers());

        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showInitialPlayerStatus() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showStarterPlacement(String username) {
        // update only user that placed the card
        if (controller.getMainPlayerUsername().equals(username)) {
            availableActions.add(TUIActions.COLOR);
            availableActions.remove(TUIActions.STARTER);

            // resources may have changed
            ClientUtil.printResourcesArea(this.controller.getMainPlayer().getPlayground().getResources());

            try {
                currOffset = ClientUtil.printPlayground(this.controller.getMainPlayerPlayground(), currOffset);
            } catch (UndrawablePlaygroundException e) {
                ClientUtil.writeLine(GameScreenArea.INPUT_AREA.getScreenPosition().getX() + 11,
                        GameScreenArea.INPUT_AREA.getScreenPosition().getY() + 1,
                        GameScreenArea.INPUT_AREA.getWidth() - 2,
                        e.getMessage());
            }
            ClientUtil.printPlayerHand(controller.getMainPlayerCards(), cardSide);

            ClientUtil.putCursorToInputArea();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(String username) { //showUpdateColor shows the new scoreBoard with the updated colors
        ClientUtil.printScoreboard(this.controller.getPlayers());

        if (controller.getMainPlayerUsername().equals(username)) {
            setAvailableActions();
        }

        ClientUtil.putCursorToInputArea();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard() {
        // print private objective card
        ClientUtil.printObjectiveCards(controller.getPlayerObjectives(), GameScreenArea.PRIVATE_OBJECTIVE);
        //print common objective cards
        ClientUtil.printObjectiveCards(controller.getObjectiveCards(), GameScreenArea.COMMON_OBJECTIVE);

        setAvailableActions();

        ClientUtil.putCursorToInputArea();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(String username) {
        // points may have changed: show to everyone
        ClientUtil.printScoreboard(this.controller.getPlayers());

        if (this.currentWatchingPlayer.equals(username)) {
            ClientUtil.printPlayerHand(controller.getPlayer(username).getPlayerCards(), cardSide);
            // print playground
            try {
                currOffset = ClientUtil.printPlayground(controller.getPlaygroundByUsername(username), currOffset);
            } catch (UndrawablePlaygroundException e) {
                ClientUtil.writeLine(GameScreenArea.INPUT_AREA.getScreenPosition().getX() + 11,
                        GameScreenArea.INPUT_AREA.getScreenPosition().getY() + 1,
                        GameScreenArea.INPUT_AREA.getWidth() - 2,
                        e.getMessage());
            }
            // resources may have changed
            ClientUtil.printResourcesArea(controller.getPlaygroundByUsername(username).getResources());
            ClientUtil.putCursorToInputArea();

            setAvailableActions();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(String username) {
        // faceUpCards
        ClientUtil.printFaceUpCards(controller.getFaceUpCards());
        // print decks
        ClientUtil.printDecks(controller.getGoldenDeckTopBack(), controller.getResourceDeckTopBack());
        // print hand
        ClientUtil.printPlayerHand(controller.getPlayer(currentWatchingPlayer).getPlayerCards(), cardSide);

        setAvailableActions();

        ClientUtil.putCursorToInputArea();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat() {
        ClientUtil.printChat(controller.getMessages(), controller.getMainPlayer());

        ClientUtil.putCursorToInputArea();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer() {
        String currentUsername = controller.getCurrentPlayerUsername();
        String myUsername = controller.getMainPlayerUsername();
        // return to my playground if it's my turn, and I'm spying someone
        if (myUsername.equals(currentUsername) && !myUsername.equals(currentWatchingPlayer)) {
            currentWatchingPlayer = myUsername;

            showUpdateAfterPlace(currentUsername);
        }

        String currentPlayerPrint = myUsername.equals(currentUsername) ?
                "your" : currentUsername + "'s";
        ClientUtil.printCommand("It's " + currentPlayerPrint + " turn");
        setAvailableActions();
        ClientUtil.putCursorToInputArea();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateSuspendedGame() {
        ClientUtil.printScoreboard(this.controller.getPlayers());
        boolean isActive = controller.isGameActive();
        if (isActive) {
            ClientUtil.printCommand(" GAME IS NOW ACTIVE \n");
        } else {
            ClientUtil.printCommand(" SUSPENDED GAME \n");
        }
        ClientUtil.putCursorToInputArea();

        setAvailableActions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) {
        ClientUtil.printCommand("Winners\n");
        for (String i : winners) {
            ClientUtil.printCommand(i + "\n");
        }
    }

    @Override
    public void reportError(String details) {
        ClientUtil.printCommand("Error: " + details);
    }

}