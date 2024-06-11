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
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.View;

import java.rmi.RemoteException;
import java.util.*;

public class ClientTUI implements View {
    private final Scanner console;
    private Set<TUIActions> availableActions = new HashSet<>();

    private Side cardSide = Side.FRONT;

    private final ClientController controller;

    /**
     * Constructs clientTUI with the <code>controller</code> provided.
     *
     * @param controller the representation of the client controller.
     */
    public ClientTUI(ClientController controller) {
        this.controller = controller;
        this.console = new Scanner(System.in);

        availableActions.add(TUIActions.HELP);
        availableActions.add(TUIActions.QUIT);
    }

    /**
     * Parses the player's commands.
     */
    private void parseGameCommands() {
        // TODO: command condition should be changed
        while (console.hasNextLine()) {
            // split command with spaces and analyze the first word
            String[] nextCommand = console.nextLine().toLowerCase().split(" ", 2);

            try {
                if (availableActions.contains(TUIActions.valueOf(nextCommand[0].toUpperCase()))) {
                    switch (TUIActions.valueOf(nextCommand[0].toUpperCase())) {
                        case BACK -> goBack();
                        case COLOR -> chooseColor();
                        case FLIP -> flip();
                        case HELP -> ClientUtil.printHelpCommands(availableActions);
                        case SPY -> spy(Integer.parseInt(nextCommand[1]) - 1);
                        case M, PM -> sendMessage(nextCommand);
                        case DRAW -> draw();
                        case PLACE -> place();
                        case QUIT -> quit();
                        case LOBBYSIZE -> setupLobbyPlayerNumber(Integer.parseInt(nextCommand.length == 2 ? nextCommand[1] : "0")); //todo: could be done in a better way?
                        case OBJECTIVE -> chooseObjective();
                        case STARTER -> placeStarter();
                        case RULEBOOK -> displayRulebook();
                    }
                } else {
                    ClientUtil.printCommand("Invalid game command");
                    // print help for consented commands
                    ClientUtil.printHelpCommands(availableActions);
                }
            } catch (IllegalArgumentException | IndexOutOfBoundsException | InvalidPlayersNumberException |
                     RemoteException | InvalidMessageException | InvalidIdForDrawingException | EmptyDeckException |
                     InvalidColorException | NotExistingFaceUp | Playground.UnavailablePositionException |
                     Playground.NotEnoughResourcesException | InvalidGamePhaseException | SuspendedGameException e) {
                ClientUtil.clearExceptionSpace();
                ClientUtil.writeLine(GameScreenArea.INPUT_AREA.getScreenPosition().getX()+11,
                        GameScreenArea.INPUT_AREA.getScreenPosition().getY()+1,
                        GameScreenArea.INPUT_AREA.getWidth()-2,
                        e.getMessage());
                // print help for consented commands
                ClientUtil.printHelpCommands(availableActions);
            } finally {
                ClientUtil.putCursorToInputArea();
            }
        }
    }

    /**
     * Restores the current playing area of the player.
     */
    private void goBack() {
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
     *
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    private void quit() throws RemoteException {
        controller.disconnect(controller.getMainPlayerUsername());

        System.exit(0);
    }

    /**
     * Receives the index of the secret objective card chosen by the player.
     *
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException if the game phase doesn't allow choosing objective cards.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws NumberFormatException     if the player digits an entry that isn't a number.
     */
    private void chooseObjective() throws RemoteException, InvalidGamePhaseException, SuspendedGameException, NumberFormatException {
        ClientUtil.printCommand("Choose objective idx: ");
        int objectiveIdx = Integer.parseInt(console.nextLine()); // starting from 1
        controller.placeObjectiveCard(objectiveIdx - 1);

        // clear input area
        ClientUtil.printCommandSquare();
        //ClientUtil.putCursorToInputArea();
    }

    /**
     * Sets the number of players allowed in the lobby.
     *
     * @param size of the lobby.
     * @throws InvalidPlayersNumberException if the <code>size</code> is greater than 4 or less than 2
     * @throws RemoteException               in the event of an error occurring during the execution of a remote method.
     * @throws NumberFormatException         if the player digits an entry that isn't a number.
     */
    private void setupLobbyPlayerNumber(int size) throws InvalidPlayersNumberException, RemoteException, NumberFormatException {
        controller.setPlayersNumber(size);
        // remove manually: only creator has this command
        availableActions.remove(TUIActions.LOBBYSIZE);

        //ClientUtil.putCursorToInputArea();
    }

    /**
     * Receives the color chosen by the player.
     *
     * @throws InvalidColorException     if the color has already been selected by others.
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException if the game phase doesn't allow choosing colors.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws IllegalArgumentException  if the color doesn't match with any <code>PlayerColor</code> color.
     */
    private void chooseColor() throws InvalidColorException, RemoteException, InvalidGamePhaseException, SuspendedGameException, IllegalArgumentException {
        ClientUtil.printCommand("Choose color: ");
        PlayerColor color = PlayerColor.valueOf(console.nextLine().toUpperCase());
        controller.chooseColor(color);

        // clear input area
        ClientUtil.printCommandSquare();
        //ClientUtil.putCursorToInputArea();
    }

    /**
     * Sends a message
     *
     * @param command an array of strings containing the message and the form in which the message is to be transmitted.
     * @throws InvalidMessageException   if the author doesn't match the author or the recipient doesn't exist.
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws IndexOutOfBoundsException if the array of strings is out of range.
     */
    private void sendMessage(String[] command) throws InvalidMessageException, RemoteException, IndexOutOfBoundsException {
        String commandContent = command[1];
        Message myMessage = command[0].equals("pm") ? createPrivateMessage(commandContent) : createBroadcastMessage(commandContent);
        controller.sendMessage(myMessage);

        // clear input area
        ClientUtil.printCommandSquare();
        //ClientUtil.putCursorToInputArea();
    }

    /**
     * Creates a private message
     *
     * @param messageDetails the recipient and the content of the message.
     * @return a message with an author, a recipient and the content of the message.
     */
    private Message createPrivateMessage(String messageDetails) {
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
    private Message createBroadcastMessage(String messageContent) {
        return new Message(controller.getMainPlayerUsername(), messageContent);
    }

    /**
     * Receives the position of the card to draw.
     *
     * @throws InvalidIdForDrawingException if the id isn't valid for drawing.
     * @throws EmptyDeckException           in the event that the deck is empty.
     * @throws NotExistingFaceUp            if the face up slot is empty.
     * @throws RemoteException              in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException    if the game doesn't allow to draw cards.
     * @throws SuspendedGameException       if the game is suspended.
     */
    private void draw() throws InvalidIdForDrawingException, EmptyDeckException, NotExistingFaceUp, RemoteException, InvalidGamePhaseException, SuspendedGameException {
        ClientUtil.printCommand("Insert the position of the card you want to draw: ");
        int drawFromId = Integer.parseInt(console.nextLine()) - 1;
        controller.draw(drawFromId);

        //ClientUtil.putCursorToInputArea();
    }

    /**
     * Places a specific card on a specific side at a specific position of the playground.
     *
     * @throws Playground.UnavailablePositionException if the position isn't available or if it is already occupied.
     * @throws Playground.NotEnoughResourcesException  if the needed resources to place the card aren't enough.
     * @throws RemoteException                         in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException               if the game phase doesn't allow placing cards.
     * @throws SuspendedGameException                  if the game is suspended.
     * @throws InputMismatchException                  if the entry made by the player does not match the pattern of the type of entry
     *                                                 that is expected.
     */
    private void place() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, RemoteException, InvalidGamePhaseException, SuspendedGameException, InputMismatchException {
        int handPos = receivePlayerCardPosition();
        Side cardSide = receiveSide();
        Position newCardPos = receivePosition();
        controller.placeCard(handPos, cardSide, newCardPos);
    }

    /**
     * Places the starter card on the given side.
     *
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException if the game doesn't allow placing starter cards.
     * @throws SuspendedGameException    if the game is suspended.
     */
    private void placeStarter() throws RemoteException, InvalidGamePhaseException, SuspendedGameException {
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
     */
    private Side receiveSide() {
        ClientUtil.printCommand("What side of the card you want to place, front or back? ");
        return Side.valueOf(console.nextLine().toUpperCase());
    }

    /**
     * Receives the position of the card in the player's hand.
     *
     * @return the position of the card if it is valid, a runtime exception otherwise.
     */
    private int receivePlayerCardPosition() {
        ClientUtil.printCommand("Enter card position in your hand: ");
        int cardPosition = Integer.parseInt(console.nextLine());

        ClientUtil.putCursorToInputArea();

        if(cardPosition >= 1 && cardPosition <= 3) return cardPosition - 1;
        // todo: change exception
        else throw new RuntimeException("not a valid card position");
    }

    /**
     * Receives a position of the playground.
     *
     * @return the position.
     */
    private Position receivePosition() {
        ClientUtil.printCommand("Insert position, with x and y space separated (e.g.: 1 2): ");
        // todo: handle wrong input
        int x = console.nextInt();
        int y = console.nextInt();
        //ClientUtil.putCursorToInputArea();
        console.nextLine();
        return new Position(x, y);
    }

    /**
     * Displays the rulebook on the screen.
     */
    private void displayRulebook(){
        ClientUtil.printCommand("Insert page (1 or 2): ");
        int numberOfPage=Integer.parseInt(console.nextLine());
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
     * This method is invoked in a new thread at the beginning of a game
     * Commands can't be interrupted
     */
    @Override
    public void runView(VirtualView client) {
        ClientUtil.clearScreen();
        // connection logic
        while(true) {
            try {
                System.out.print("Insert username: ");
                String username = console.nextLine();
                controller.connect(client, username);
                client.setName(username);
                break;
            } catch(InvalidUsernameException | FullLobbyException | RemoteException e){
                System.err.println(e.getMessage());
            }
        }

        new Thread(this::parseGameCommands).start();
    }

    /**
     * Displays the playground of the <code>playerIdx</code> player.
     *
     * @param playerIdx the index of the player to spy.
     */
    private void spy(int playerIdx) {
        ClientPlayer player = this.controller.getPlayers().get(playerIdx);
        ClientPlayground playground = player.getPlayground();

        if (this.controller.getMainPlayer().equals(player)) {
            throw new IllegalArgumentException("you can't spy yourself...");
        } else {
            // update commands when you are looking at other players
            availableActions.remove(TUIActions.DRAW);
            availableActions.remove(TUIActions.PLACE);
            availableActions.remove(TUIActions.FLIP);

            availableActions.add(TUIActions.BACK);
            // update only playground, hand and resources
            ClientUtil.printResourcesArea(playground.getResources());
            ClientUtil.printPlayground(playground);
            ClientUtil.printPlayerHand(this.controller.getMainPlayerCards(), cardSide);
        }
    }

    /**
     * Sets available actions in accordance to the current phase of the game.
     */
    private void setAvailableActions() {
        GamePhase currentPhase = controller.getGamePhase();

        Set<TUIActions> availableActions = new HashSet<>();
        availableActions.add(TUIActions.HELP);
        availableActions.add(TUIActions.QUIT);

        // add actions only if game is active
        if(controller.isGameActive()){
            if (currentPhase != null) { // if not in lobby
                availableActions.add(TUIActions.M);
                availableActions.add(TUIActions.PM);
                availableActions.add(TUIActions.FLIP);
                availableActions.add(TUIActions.RULEBOOK);
            }

            switch (currentPhase) {
                case Setup -> {
                    // starter command available only if user have to do starter stuff
                    if (controller.isMainPlaygroundEmpty()) {
                        availableActions.add(TUIActions.STARTER);
                    } else if (controller.getMainColor() == null) {
                        availableActions.add(TUIActions.COLOR);
                    } else {
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

        this.availableActions = availableActions;
    }

    // SHOW UPDATE

    /**
     * {@inheritDoc}
     */
    @Override
    public void showServerCrash() {
        ClientUtil.printCommand("Server is crashed. To connect again you have to join the game");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby() {
        List<String> usernames = controller.getUsernames();
        System.out.println("Users waiting: <" + String.join(",", usernames) + ">");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCreator() {
        System.out.println("Welcome to the new lobby!");
        System.out.println("Please set the lobby size (2 to 4 players allowed)");
        System.out.println("Type 'lobbysize <number>' to set the lobby size");

        // add manually: only creator has this command
        availableActions.add(TUIActions.LOBBYSIZE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterLobbyCrash() {
        ClientUtil.printCommand("Lobby crashed! You will be disconnected. Please restart the client...");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterConnection() {
        ClientUtil.clearScreen();
        setAvailableActions();

        ClientPlayground playerPlayground = this.controller.getMainPlayerPlayground();

        ClientUtil.printScoreboard(this.controller.getPlayers());
        ClientUtil.printResourcesArea(playerPlayground.getResources());
        ClientUtil.printFaceUpCards(this.controller.getFaceUpCards());
        ClientUtil.printCommandSquare();
        ClientUtil.printChatSquare();
        // print decks
        ClientUtil.printDecks(controller.getResourceDeckTopBack(), controller.getGoldenDeckTopBack());

        // print objective(s)
        showUpdateObjectiveCard();

        ClientUtil.printPlayground(playerPlayground);
        // check if there is any occupied tile: it means starter has been placed
        ClientUtil.printPlayerHand(controller.isMainPlaygroundEmpty() ?
                Collections.singletonList(this.controller.getMainPlayerStarter()) :
                controller.getMainPlayerCards(),
                cardSide);

        ClientUtil.putCursorToInputArea();
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

            ClientUtil.printPlayground(this.controller.getMainPlayerPlayground());
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
        if (this.controller.getMainPlayerUsername().equals(username)) {
            ClientUtil.printPlayerHand(this.controller.getMainPlayerCards(), cardSide);
            // print playground
            ClientUtil.printPlayground(this.controller.getMainPlayerPlayground());
            // resources may have changed
            ClientUtil.printResourcesArea(this.controller.getMainPlayer().getPlayground().getResources());
            ClientUtil.putCursorToInputArea();

            setAvailableActions();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw() {
        // faceUpCards
        ClientUtil.printFaceUpCards(this.controller.getFaceUpCards());
        // print decks
        ClientUtil.printDecks(controller.getResourceDeckTopBack(), controller.getGoldenDeckTopBack());
        // print hand
        ClientUtil.printPlayerHand(this.controller.getMainPlayerCards(), cardSide);

        setAvailableActions();

        ClientUtil.putCursorToInputArea();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat() {
        ClientUtil.printChat(controller.getMessage());

        ClientUtil.putCursorToInputArea();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer() {
        String currentPlayerPrint = controller.getCurrentPlayerUsername().equals(controller.getMainPlayerUsername()) ?
                "your" : controller.getCurrentPlayerUsername() + "'s";
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
            ClientUtil.printCommand("\u001B[1m GAME IS NOW ACTIVE \u001B[0m\n");
        } else {
            ClientUtil.printCommand("\u001B[1m SUSPENDED GAME \u001B[0m\n");
        }
        ClientUtil.putCursorToInputArea();

        setAvailableActions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) {
        ClientUtil.printCommand("\u001B[1m\u001B[5m" + "Winners\n" + "\u001B[0m\u001B[5m");
        for (String i : winners) {
            ClientUtil.printCommand("\u001B[3m" + i + "\u001B[0m\n");
        }

    }

}
