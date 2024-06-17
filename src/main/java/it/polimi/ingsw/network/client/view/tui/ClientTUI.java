package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.controller.InvalidIdForDrawingException;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Availability;
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
import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.View;

import java.rmi.RemoteException;
import java.util.*;

public class ClientTUI implements View {
    private final Scanner console;
    private Set<TUIActions> availableActions = new HashSet<>();

    private Side cardSide = Side.FRONT;

    private final ClientController controller;

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

    private void parseGameCommands() {
        // TODO: command condition should be changed
        while (console.hasNextLine()) {
            // split command with spaces and analyze the first word
            String[] nextCommand = console.nextLine().toLowerCase().split(" ", 3);

            try {
                if (availableActions.contains(TUIActions.valueOf(nextCommand[0].toUpperCase()))) {
                    switch (TUIActions.valueOf(nextCommand[0].toUpperCase())) {
                        case CONNECT -> connect(nextCommand[1], Integer.parseInt(nextCommand[2]));
                        case SELECTUSERNAME -> selectUsername(nextCommand[1]);
                        case BACK -> goBack();
                        case COLOR -> chooseColor();
                        case FLIP -> flip();
                        case HELP -> ClientUtil.printHelpCommands(availableActions);
                        case SPY -> lookAtPlayer(Integer.parseInt(nextCommand[1]) - 1);
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
                     InvalidMessageException | InvalidIdForDrawingException | EmptyDeckException |
                     InvalidColorException | NotExistingFaceUp | Playground.UnavailablePositionException |
                     Playground.NotEnoughResourcesException | InvalidGamePhaseException | SuspendedGameException e) {
                ClientUtil.printToLineColumn(GameScreenArea.INPUT_AREA.getScreenPosition().getX() + 2, GameScreenArea.INPUT_AREA.getScreenPosition().getY() + 1, e.getMessage());
                // print help for consented commands
                ClientUtil.printHelpCommands(availableActions);
            } catch (UnReachableServerException e) {
                System.out.println("Server is down, please connect to another server");
                availableActions.clear();
                availableActions.add(TUIActions.HELP);
                availableActions.add(TUIActions.QUIT);
                availableActions.add(TUIActions.CONNECT);
            } finally {
                ClientUtil.putCursorToInputArea();
            }
        }
    }

    private void goBack() {
        // add back commands
        setAvailableActions();
        // clear screen
        ClientUtil.clearScreen();
        // print main player stuff again
        showUpdateAfterConnection();
        ClientUtil.printPlayground(this.controller.getMainPlayerPlayground());
        ClientUtil.printPlayerHand(this.controller.getMainPlayerCards(), cardSide);
        if(controller.getGamePhase()!=GamePhase.Setup ){
            showUpdateObjectiveCard();
        }

    }

    private void flip() {
        // invert side
        cardSide = cardSide.equals(Side.FRONT) ? Side.BACK : Side.FRONT;

        ClientUtil.printPlayerHand(this.controller.getMainPlayerCards(), cardSide);
    }

    private void quit() {
        controller.disconnect(controller.getMainPlayerUsername());

        System.exit(0);
    }

    private void chooseObjective() throws InvalidGamePhaseException, SuspendedGameException, NumberFormatException {
        ClientUtil.printCommand("Choose objective idx: ");
        int objectiveIdx = Integer.parseInt(console.nextLine()); // starting from 1
        controller.placeObjectiveCard(objectiveIdx - 1);

        ClientUtil.putCursorToInputArea();
    }

    private void setupLobbyPlayerNumber(int size) throws InvalidPlayersNumberException, NumberFormatException {
        controller.setPlayersNumber(size);
        // remove manually: only creator has this command
        availableActions.remove(TUIActions.LOBBYSIZE);

        ClientUtil.putCursorToInputArea();
    }

    private void chooseColor() throws InvalidColorException, InvalidGamePhaseException, SuspendedGameException, IllegalArgumentException {
        ClientUtil.printCommand("Choose color: ");
        PlayerColor color = PlayerColor.valueOf(console.nextLine().toUpperCase());
        controller.chooseColor(color);

        ClientUtil.putCursorToInputArea();
    }

    private void sendMessage(String[] command) throws InvalidMessageException, IndexOutOfBoundsException {
        String commandContent = command[1];
        Message myMessage = command[0].equals("pm") ? createPrivateMessage(commandContent) : createBroadcastMessage(commandContent);
        controller.sendMessage(myMessage);

        ClientUtil.putCursorToInputArea();
    }

    private Message createPrivateMessage(String messageDetails) throws InvalidMessageException {
        // messageDetails contains recipient too
        String[] messageSplit = messageDetails.split(" ", 2);
        String recipient = messageSplit[0];
        String messageContent = messageSplit[1];
        return new Message(controller.getMainPlayerUsername(), recipient, messageContent);
    }

    private Message createBroadcastMessage(String messageContent) throws InvalidMessageException {
        return new Message(controller.getMainPlayerUsername(), messageContent);
    }

    private void draw() throws InvalidIdForDrawingException, EmptyDeckException, NotExistingFaceUp, InvalidGamePhaseException, SuspendedGameException {
        ClientUtil.printCommand("Insert the position of the card you want to draw: ");
        int drawFromId = Integer.parseInt(console.nextLine()) - 1;
        controller.draw(drawFromId);

        ClientUtil.putCursorToInputArea();
    }

    private void place() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, InputMismatchException {
        int handPos = receivePlayerCardPosition();
        Side cardSide = receiveSide();
        Position newCardPos = receivePosition();
        controller.placeCard(handPos, cardSide, newCardPos);
    }

    private void placeStarter() throws InvalidGamePhaseException, SuspendedGameException {
        Side starterSide = receiveSide();
        controller.placeStarter(starterSide);

        ClientUtil.putCursorToInputArea();
    }

    private Side receiveSide() {
        ClientUtil.printCommand("What side of the card you want to place, front or back? ");
        return Side.valueOf(console.nextLine().toUpperCase());
    }

    private int receivePlayerCardPosition() {
        ClientUtil.printCommand("Enter card position in your hand: ");
        int cardPosition = Integer.parseInt(console.nextLine());

        ClientUtil.putCursorToInputArea();

        if(cardPosition >= 1 && cardPosition <= 3) return cardPosition - 1;
        // todo: change exception
        else throw new RuntimeException("not a valid card position");
    }

    private Position receivePosition() {
        ClientUtil.printCommand("Insert position, with x and y space separated (e.g.: 1 2): ");
        // todo: handle wrong input
        int x = console.nextInt();
        int y = console.nextInt();
        ClientUtil.putCursorToInputArea();
        return new Position(x, y);
    }

    private void displayRulebook(){
        ClientUtil.printRulebook();
        availableActions.add(TUIActions.BACK);
        ClientUtil.putCursorToInputArea();
    }

    //private void getInfoForConnection() {
    //    System.out.println("Insert the ip of the server (empty for localhost)");
    //    String ip = console.nextLine();
    //    System.out.println("Insert the port of the server");
    //    String port = console.nextLine();
    //    try {
    //        if (ip.isEmpty()) {
    //            ip = "127.0.0.1";
    //        }
    //        controller.configureClient(this, ip, port);
    //    } catch(UnReachableServerException e) {
    //        System.err.println("Error during connection to the server: " + e.getMessage());
    //        System.exit(1);
    //    }
    //}

    //private void getUsername() {
    //    while (true) {
    //        try {
    //            System.out.print("Insert username: ");
    //            String username = console.nextLine();
    //            controller.connect(username);
    //            break;
    //        } catch (RemoteException e) {
    //            System.err.println(e.getMessage());
    //            break;
    //        }
    //    }
    //}

    private void connect(String ip, Integer port) throws UnReachableServerException {
        controller.configureClient(this, ip, port);
        availableActions.remove(TUIActions.CONNECT);
        System.out.println("Insert your username: type <selectusername> <your username>");
        availableActions.add(TUIActions.SELECTUSERNAME);
    }

    private void selectUsername(String username) {
        availableActions.remove(TUIActions.SELECTUSERNAME);
        controller.connect(username);
    }

    /**
     * This method is invoked in a new thread at the beginning of a game
     * Commands can't be interrupted
     */
    @Override
    public void runView() {
        System.out.println("Welcome. To play connect to the server: type connect <ip> <port>");
        //getUsername();
        //while(true) {
        //    try {
        //        System.out.print("Insert username: ");
        //        String username = console.nextLine();
        //        controller.connect(username);
        //        break;
        //    } catch(RemoteException e){
        //        System.err.println(e.getMessage());
        //    }
        //}
        // todo: synchronize to have correct command list
        new Thread(this::parseGameCommands).start();
    }

    private void lookAtPlayer(int playerIdx) {
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

    // SHOW UPDATE

    @Override
    public void showServerCrash() {
        ClientUtil.printCommand("Server is crashed. To connect again you have to join the game");
        availableActions.clear();
        setActionsForConnection();
    }

    private void setAvailableActions() {
        GamePhase currentPhase = controller.getGamePhase();

        Set<TUIActions> availableCommands = new HashSet<>();
        availableCommands.add(TUIActions.HELP);
        availableCommands.add(TUIActions.QUIT);

        if (currentPhase != null) {
            availableCommands.add(TUIActions.M);
            availableCommands.add(TUIActions.PM);
            availableCommands.add(TUIActions.FLIP);
            availableCommands.add(TUIActions.RULEBOOK);
        }

        switch (currentPhase) {
            case Setup -> {
                // starter command available only if user have to do starter stuff
                if (this.controller.getColor() == null) {
                    availableCommands.add(TUIActions.STARTER);
                }
            }
            // don't let user use unneeded commands when it's not their turn
            case DrawNormal -> {
                if (this.controller.getCurrentPlayerUsername().equals(this.controller.getMainPlayerUsername()))
                    availableCommands.add(TUIActions.DRAW);

                availableCommands.add(TUIActions.SPY);
            }
            case PlaceNormal, PlaceAdditional -> {
                if (this.controller.getCurrentPlayerUsername().equals(this.controller.getMainPlayerUsername()))
                    availableCommands.add(TUIActions.PLACE);

                availableCommands.add(TUIActions.SPY);
            }
            case null -> {}
            case End -> {}
        }

        this.availableActions = availableCommands;
    }

    @Override
    public void showUpdatePlayersInLobby() {
        List<String> usernames = controller.getUsernames();
        System.out.println("Users waiting: <" + String.join(",", usernames) + ">");
    }

    @Override
    public void showUpdateCreator() {
        System.out.println("Welcome to the new lobby!");
        System.out.println("Please set the lobby size (2 to 4 players allowed)");
        System.out.println("Type 'lobbysize <number>' to set the lobby size");

        // add manually: only creator has this command
        availableActions.add(TUIActions.LOBBYSIZE);
    }

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
        System.out.println("Invalid username! Reason: " + details);
        System.out.println("Please insert a new username with the command <selectusername> <your name>");
        availableActions.add(TUIActions.SELECTUSERNAME);
    }

    @Override
    public void showUpdateAfterConnection() {
        ClientUtil.clearScreen();
        setAvailableActions();

        ClientUtil.printPlayerHand(this.controller.getMainPlayerCards(), cardSide);
        ClientUtil.printScoreboard(this.controller.getPlayers());
        ClientUtil.printResourcesArea(this.controller.getMainPlayer().getPlayground().getResources());
        ClientUtil.printFaceUpCards(this.controller.getFaceUpCards().stream().map(c -> c.getFace(Side.FRONT)).toList());
        ClientUtil.printCommandSquare();
        ClientUtil.printChatSquare();
        // todo: do not print if starter card hasn't been placed
        if (this.controller.getMainPlayer().getPlayground().getArea().values().stream()
                .anyMatch(tile -> tile.sameAvailability(Availability.OCCUPIED))) {
            ClientUtil.printPlayground(this.controller.getMainPlayerPlayground());
        }

        ClientUtil.putCursorToInputArea();
    }

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
        // todo: print starter side before, not after
//        ClientUtil.printInLineColumn(23,64,ClientUtil.designCard(controller.getMainPlayer().getStarterCard().getFront()));
//        ClientUtil.printInLineColumn(23,64+ClientUtil.cardWidth+2,ClientUtil.designCard(controller.getMainPlayer().getStarterCard().getBack()));
        // update only user that placed the card
        if (controller.getMainPlayerUsername().equals(username)) {
            availableActions.add(TUIActions.COLOR);
            availableActions.remove(TUIActions.STARTER);

            // resources may have changed
            ClientUtil.printResourcesArea(this.controller.getMainPlayer().getPlayground().getResources());

            ClientUtil.printPlayground(this.controller.getMainPlayerPlayground());

            ClientUtil.putCursorToInputArea();
        }
    }

    @Override
    public void showUpdateColor(String username) { //showUpdateColor shows the new scoreBoard with the updated colors
        ClientUtil.printScoreboard(this.controller.getPlayers());

        if (controller.getMainPlayerUsername().equals(username)) {
            availableActions.remove(TUIActions.COLOR);
            availableActions.add(TUIActions.OBJECTIVE);
        }

        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showUpdateObjectiveCard() {
        // print private objective card
        ClientUtil.printToLineColumn(GameScreenArea.PRIVATE_OBJECTIVE.getScreenPosition().getX(), GameScreenArea.PRIVATE_OBJECTIVE.getScreenPosition().getY(), ClientUtil.designObjectiveCard(controller.getMainPlayer().getObjectiveCards().getFirst()));

        //print common objective cards
        for (int i = 0; i < 2; i++) {
            ClientUtil.printToLineColumn(GameScreenArea.COMMON_OBJECTIVE.getScreenPosition().getX(), GameScreenArea.COMMON_OBJECTIVE.getScreenPosition().getY() + (ClientUtil.cardWidth + 2) * i, ClientUtil.designObjectiveCard(controller.getObjectiveCards().get(i)));
        }

        // remove action: new actions will be available after current player update
        availableActions.remove(TUIActions.OBJECTIVE);

        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showUpdateAfterPlace(String username) {
        if (this.controller.getMainPlayerUsername().equals(username)) {
            List<ClientFace> facesToPrint = this.controller.getMainPlayer().getPlayerCards().stream()
                    .map(c -> c.getFace(cardSide)).toList();
            ClientUtil.printPlayerHand(this.controller.getMainPlayerCards(), cardSide);
            // print playground
            ClientUtil.printPlayground(this.controller.getMainPlayerPlayground());
            // resources may have changed
            ClientUtil.printResourcesArea(this.controller.getMainPlayer().getPlayground().getResources());
            ClientUtil.putCursorToInputArea();

            setAvailableActions();
        }
    }

    @Override
    public void showUpdateAfterDraw(String username) {
        // faceUpCards
        ClientUtil.printFaceUpCards(this.controller.getFaceUpCards().stream().map(c -> c.getFace(Side.FRONT)).toList());

        //print GoldenDeckTopBack
        ClientUtil.printToLineColumn(GameScreenArea.DECKS.getScreenPosition().getX(), GameScreenArea.DECKS.getScreenPosition().getY(), ClientUtil.designCard(controller.getGoldenDeckTopBack()));

        //print ResourceDeckTopBack
        ClientUtil.printToLineColumn(GameScreenArea.DECKS.getScreenPosition().getX(), GameScreenArea.DECKS.getScreenPosition().getY() + ClientUtil.cardWidth + 2, ClientUtil.designCard(controller.getResourceDeckTopBack()));
        // print hand
        ClientUtil.printPlayerHand(this.controller.getMainPlayerCards(), cardSide);

        setAvailableActions();

        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showUpdateChat() {
        ClientUtil.printChat(controller.getMessages());

        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showUpdateCurrentPlayer() {
        ClientUtil.printCommand("It's " + this.controller.getCurrentPlayerUsername() + "'s turn");
        setAvailableActions();
        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showUpdateSuspendedGame() {
        ClientUtil.printScoreboard(this.controller.getPlayers());
        boolean isActive = controller.isActive();
        if (isActive) {
            ClientUtil.printCommand("\u001B[1m GAME IS NOW ACTIVE \u001B[0m\n");
        } else {
            ClientUtil.printCommand("\u001B[1m SUSPENDED GAME \u001B[0m\n");
        }
        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showWinners(List<String> winners) {
        ClientUtil.printCommand("\u001B[1m\u001B[5m" + "Winners\n" + "\u001B[0m\u001B[5m");
        for (String i : winners) {
            ClientUtil.printCommand("\u001B[3m" + i + "\u001B[0m\n");
        }

    }

    @Override
    public void reportError(String details) {
        ClientUtil.printCommand("Error: " + details);
    }

}
