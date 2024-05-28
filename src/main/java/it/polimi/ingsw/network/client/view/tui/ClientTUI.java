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
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.View;

import java.rmi.RemoteException;
import java.util.*;

public class ClientTUI implements View {
    private final Scanner console;
    private Set<TUIActions> availableActions = new HashSet<>();

    private final ClientController controller;

    public ClientTUI(ClientController controller) {
        this.controller = controller;
        this.console = new Scanner(System.in);

        availableActions.add(TUIActions.HELP);
        availableActions.add(TUIActions.QUIT);
    }

    private void parseGameCommands() {
        // TODO: command condition should be changed
        while (console.hasNextLine()) {
            // split command with spaces and analyze the first word
            String[] nextCommand = console.nextLine().toLowerCase().split(" ", 2);

            try {
                if (availableActions.contains(TUIActions.valueOf(nextCommand[0].toUpperCase()))) {
                    switch (TUIActions.valueOf(nextCommand[0].toUpperCase())) {
                        case BACK -> {
                            // print main player stuff again
                            ClientUtil.printPlayground(this.controller.getMainPlayerPlayground());
                            ClientUtil.printPlayerHand(this.controller.getMainPlayer().getPlayerCards());
                        }
                        case COLOR -> chooseColor();
                        case HELP -> ClientUtil.printHelpCommands(availableActions);
                        case SPY -> lookAtPlayer(Integer.parseInt(nextCommand[1]) - 1);
                        case M, PM -> sendMessage(nextCommand);
                        case DRAW -> draw();
                        case PLACE -> place();
                        case QUIT -> quit();
                        case LOBBYSIZE -> setupLobbyPlayerNumber(Integer.parseInt(nextCommand.length == 2 ? nextCommand[1] : "0")); //todo: could be done in a better way?
                        case OBJECTIVE -> chooseObjective();
                        case STARTER -> placeStarter();
                    }
                } else {
                    System.out.println("Invalid game command");
                    // print help for consented commands
                    ClientUtil.printHelpCommands(availableActions);
                }
            } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
                // print help for consented commands
                ClientUtil.printHelpCommands(availableActions);
            } finally {
                ClientUtil.putCursorToInputArea();
            }
        }
    }

    private void quit() {
        try {
            controller.disconnect(controller.getMainPlayerUsername());
        } catch (RemoteException e) {
            System.out.println("Error disconnecting");
        }

        System.exit(0);
    }

    private void chooseObjective() {
        System.out.println("Choose objective idx: ");
        try {
            int objectiveIdx = Integer.parseInt(console.nextLine()); // starting from 1
            controller.placeObjectiveCard(objectiveIdx - 1);
        } catch (RemoteException | InvalidGamePhaseException | SuspendedGameException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setupLobbyPlayerNumber(int size) {
        try {
            controller.setPlayersNumber(size);
            // remove manually: only creator has this command
            availableActions.remove(TUIActions.LOBBYSIZE);
        } catch (InvalidPlayersNumberException | RemoteException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    private void chooseColor() {
        System.out.println("Choose color: ");
        try {
            PlayerColor color = PlayerColor.valueOf(console.nextLine().toUpperCase());
            controller.chooseColor(color);
        } catch (InvalidColorException | RemoteException | InvalidGamePhaseException | SuspendedGameException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private void sendMessage(String[] command) {
        try {
            String commandContent = command[1];
            Message myMessage = command[0].equals("pm") ? createPrivateMessage(commandContent) : createBroadcastMessage(commandContent);
            controller.sendMessage(myMessage);
        } catch (InvalidMessageException | RemoteException | IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    private Message createPrivateMessage(String messageDetails) {
        // messageDetails contains recipient too
        String[] messageSplit = messageDetails.split(" ", 2);
        String recipient = messageSplit[0];
        String messageContent = messageSplit[1];
        return new Message(controller.getMainPlayerUsername(), recipient, messageContent);
    }

    private Message createBroadcastMessage(String messageContent) {
        return new Message(controller.getMainPlayerUsername(), messageContent);
    }

    private void draw() {
        System.out.print("Insert the position of the card you want to draw: ");
        try {
            int drawFromId = Integer.parseInt(console.nextLine()) - 1;
            controller.draw(drawFromId);
        } catch (InvalidIdForDrawingException | EmptyDeckException | NotExistingFaceUp | RemoteException |
                 InvalidGamePhaseException | SuspendedGameException e) {
            System.out.println(e.getMessage());
        }
    }

    private void place() {
        try {
            int handPos = receivePlayerCardPosition();
            Side cardSide = receiveSide();
            Position newCardPos = receivePosition();
            controller.placeCard(handPos, cardSide, newCardPos);
        } catch (Playground.UnavailablePositionException | Playground.NotEnoughResourcesException | RemoteException |
                 InvalidGamePhaseException | SuspendedGameException | InputMismatchException e) {
            System.out.println(e.getMessage());
        }
    }

    private void placeStarter() {
        try {
            Side starterSide = receiveSide();
            controller.placeStarter(starterSide);
        } catch (RemoteException | InvalidGamePhaseException | SuspendedGameException e) {
            System.out.println(e.getMessage());
        }
    }

    private Side receiveSide() {
        System.out.print("What side of the card you want to place, front or back?");
        return Side.valueOf(console.nextLine().toUpperCase());
    }

    private int receivePlayerCardPosition() {
        System.out.print("Enter card position in your hand: ");
        int cardPosition = Integer.parseInt(console.nextLine());

        if(cardPosition >= 1 && cardPosition <= 3) return cardPosition - 1;
        // todo: change exception
        else throw new RuntimeException("not a valid card position");
    }

    private Position receivePosition() {
        System.out.print("Insert position, with x and y space separated (e.g.: 1 2): ");
        // todo: handle wrong input
        int x = console.nextInt();
        int y = console.nextInt();
        return new Position(x, y);
    }

    /**
     * This method is invoked in a new thread at the beginning of a game
     * Commands can't be interrupted
     */
    @Override
    public void beginCommandAcquisition() {
        // todo: synchronize to have correct command list
        new Thread(this::parseGameCommands).start();
    }


    @Override
    public ClientController run(VirtualView client) {
        // connection logic
        while(true) {
            try {
                System.out.print("Insert username: ");
                String username = console.nextLine();
                controller.connect(client, username);
                break;
            } catch(InvalidUsernameException | FullLobbyException | RemoteException e){
                System.err.println(e.getMessage());
            }
        }
        //beginCommandAcquisition();
        return controller;
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
            availableActions.add(TUIActions.BACK);
            // update only playground, hand and resources
            ClientUtil.printResourcesArea(playground.getResources());
            ClientUtil.printPlayground(playground);
            ClientUtil.printPlayerHand(player.getPlayerCards());
        }
    }

    // SHOW UPDATE

    @Override
    public void showServerCrash() {
        System.err.println("Server is crashed. To connect again you have to join the game");
    }

    private void setAvailableActions() {
        GamePhase currentPhase = controller.getGamePhase();

        Set<TUIActions> availableCommands = new HashSet<>();
        availableCommands.add(TUIActions.HELP);
        availableCommands.add(TUIActions.QUIT);

        if (currentPhase != null) {
            availableCommands.add(TUIActions.M);
            availableCommands.add(TUIActions.PM);
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
        System.out.println("Lobby crashed! You will be disconnected. Please restart the client...");
    }

    @Override
    public void showUpdateAfterConnection() {
        ClientUtil.clearScreen();
        setAvailableActions();

        ClientUtil.printPlayerHand(this.controller.getMainPlayer().getPlayerCards());
        ClientUtil.printScoreboard(this.controller.getPlayers());
        ClientUtil.printResourcesArea(this.controller.getMainPlayer().getPlayground().getResources());
        ClientUtil.printFaceUpCards(this.controller.getFaceUpCards().stream().map(c -> c.getFace(Side.FRONT)).toList());
        ClientUtil.printToLineColumn(GameScreenArea.CHAT.getScreenPosition().getX(),
                GameScreenArea.CHAT.getScreenPosition().getY(),
                ClientUtil.designSquare(GameScreenArea.CHAT.getWidth(),
                        GameScreenArea.CHAT.getHeight() - 2).toString());
        // todo: do not print if starter card hasn't been placed
        if (this.controller.getMainPlayer().getPlayground().getArea().values().stream()
                .anyMatch(tile -> tile.sameAvailability(Availability.OCCUPIED))) {
            ClientUtil.printPlayground(this.controller.getMainPlayerPlayground());
        }

        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showUpdatePlayerStatus() {
        List<String> usernames = controller.getConnectedUsernames();
        System.out.println("Connected players: <" + String.join(",", usernames) + ">");

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
            ClientUtil.printPlayerHand(this.controller.getMainPlayer().getPlayerCards());
            // print playground
            ClientUtil.printPlayground(this.controller.getMainPlayerPlayground());
            // resources may have changed
            ClientUtil.printResourcesArea(this.controller.getMainPlayer().getPlayground().getResources());
            ClientUtil.putCursorToInputArea();

            setAvailableActions();
        }
    }

    @Override
    public void showUpdateAfterDraw() {
        // faceUpCards
        ClientUtil.printFaceUpCards(this.controller.getFaceUpCards().stream().map(c -> c.getFace(Side.FRONT)).toList());

        //print GoldenDeckTopBack
        ClientUtil.printToLineColumn(GameScreenArea.DECKS.getScreenPosition().getX(), GameScreenArea.DECKS.getScreenPosition().getY(), ClientUtil.designCard(controller.getGoldenDeckTopBack()));

        //print ResourceDeckTopBack
        ClientUtil.printToLineColumn(GameScreenArea.DECKS.getScreenPosition().getX(), GameScreenArea.DECKS.getScreenPosition().getY() + ClientUtil.cardWidth + 2, ClientUtil.designCard(controller.getResourceDeckTopBack()));
        ClientUtil.printPlayerHand(this.controller.getMainPlayer().getPlayerCards());

        setAvailableActions();

        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showUpdateChat() {
        ClientUtil.printChat(controller.getMessage(), controller.getLastMessage());

        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showUpdateCurrentPlayer() {
        System.out.println("It's " + this.controller.getCurrentPlayerUsername() + "'s turn");

        setAvailableActions();
        ClientUtil.putCursorToInputArea();
    }

    @Override
    public void showUpdateSuspendedGame() {
        System.out.println("\u001B[1m SUSPENDED GAME \u001B[0m\n");
    }

    @Override
    public void showWinners(List<String> winners) {
        System.out.println("\u001B[1m\u001B[5m" + "Winners\n" + "\u001B[0m\u001B[5m");
        for (String i : winners) {
            System.out.println("\u001B[3m" + i + "\u001B[0m\n");
        }

    }

}
