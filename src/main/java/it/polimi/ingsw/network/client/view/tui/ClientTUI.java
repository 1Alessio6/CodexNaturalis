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
import it.polimi.ingsw.network.client.model.card.ClientCard;
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
                        case COLOR -> chooseColor();
                        case HELP -> ClientUtil.printHelpCommands(availableActions);
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

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid game command");
                // print help for consented commands
                ClientUtil.printHelpCommands(availableActions);
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
        String commandContent = command[1];
        Message myMessage = command[0].equals("pm") ? createPrivateMessage(commandContent) : createBroadcastMessage(commandContent);
        try {
            controller.sendMessage(myMessage);
        } catch (InvalidMessageException | RemoteException e) {
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
            int drawFromId = Integer.parseInt(console.nextLine());
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
                 InvalidGamePhaseException | SuspendedGameException e) {
            System.out.println(e.getMessage());
        }
    }

    private void placeStarter() {
        try {
            controller.placeStarter(receiveSide());
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

        if(cardPosition >= 1 && cardPosition <= 3) return cardPosition;
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
                String username = receiveUsername();
                controller.connect(client, username);
                break;
            } catch(InvalidUsernameException | FullLobbyException | RemoteException e){
                System.err.println(e.getMessage());
            }
        }
        //beginCommandAcquisition();
        return controller;
    }

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
            case DrawNormal -> availableCommands.add(TUIActions.DRAW);
            case Setup -> availableCommands.add(TUIActions.STARTER);
            case PlaceNormal, PlaceAdditional -> availableCommands.add(TUIActions.PLACE);
            case null -> {}
            case End -> {}
        }

        this.availableActions = availableCommands;
    }

    private String receiveUsername(){
        System.out.print("Insert username: ");
        return console.nextLine();
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
        System.out.println("Game is starting: you just joined");

        setAvailableActions();
    }

    @Override
    public void showUpdatePlayerStatus() {
        List<String> usernames = controller.getConnectedUsernames();
        System.out.println("Connected players: <" + String.join(",", usernames) + ">");
    }

    @Override
    public void showInitialPlayerStatus() {

    }

    public void showBoardSetUp() {
        ClientUtil.printToLineColumn(GameScreenArea.SCOREBOARD.screenPosition.getX(),GameScreenArea.SCOREBOARD.screenPosition.getY(),ClientUtil.createScoreBoard(this.controller.getPlayers()).toString());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void showStarterPlacement(String username) {
        ClientUtil.printInLineColumn(23,64,ClientUtil.designCard(controller.getMainPlayer().getStarterCard().getFront()));
        ClientUtil.printInLineColumn(23,64+ClientUtil.cardWidth+2,ClientUtil.designCard(controller.getMainPlayer().getStarterCard().getBack()));
        // update only user that placed the card
        if (controller.getMainPlayerUsername().equals(username)) {
            availableActions.add(TUIActions.COLOR);
            availableActions.remove(TUIActions.STARTER);

        }
    }

    @Override
    public void showUpdateColor(String username) { //showUpdateColor shows the new scoreBoard with the updated colors
        showBoardSetUp();

        if (controller.getMainPlayerUsername().equals(username)) {
            availableActions.remove(TUIActions.COLOR);
            availableActions.add(TUIActions.OBJECTIVE);
        }
    }

    @Override
    public void showUpdateObjectiveCard() {
        // print private objective card
        ClientUtil.printInLineColumn(GameScreenArea.PRIVATE_OBJECTIVE.getScreenPosition().getX(), GameScreenArea.PRIVATE_OBJECTIVE.getScreenPosition().getY(), ClientUtil.designObjectiveCard(controller.getMainPlayer().getObjectiveCards().getFirst()));

        //print common objective cards
        for (int i = 0; i < 2; i++) {
            ClientUtil.printInLineColumn(GameScreenArea.COMMON_OBJECTIVE.getScreenPosition().getX(), GameScreenArea.COMMON_OBJECTIVE.getScreenPosition().getY() + (ClientUtil.cardWidth + 2) * i, ClientUtil.designObjectiveCard(controller.getObjectiveCards().get(i)));
        }

        // remove action: now actions will be available after current player update
        availableActions.remove(TUIActions.OBJECTIVE);
    }

    @Override
    public void showUpdateAfterPlace() {
        setAvailableActions();
    }

    @Override
    public void showUpdateAfterDraw() {
        for(ClientCard i: controller.getMainPlayer().getPlayerCards()){
            //move cursor
            //print
            ClientUtil.designCard(i.getFront());
        }
        //move cursor
        for(ClientCard i: controller.getFaceUpCards()){
            ClientUtil.designCard(i.getFront());
            //move cursor
        }

        //print GoldenDeckTopBack
        ClientUtil.printInLineColumn(GameScreenArea.DECKS.getScreenPosition().getX(), GameScreenArea.DECKS.getScreenPosition().getY(), ClientUtil.designCard(controller.getGoldenDeckTopBack()));

        //print ResourceDeckTopBack
        ClientUtil.printInLineColumn(GameScreenArea.DECKS.getScreenPosition().getX(), GameScreenArea.DECKS.getScreenPosition().getY() + ClientUtil.cardWidth + 2, ClientUtil.designCard(controller.getResourceDeckTopBack()));

        setAvailableActions();
    }

    @Override
    public void showUpdateChat() {
        System.out.println(controller.getLastMessage());
    }

    @Override
    public void showUpdateCurrentPlayer() {
        System.out.println("It's " + this.controller.getMainPlayerUsername() + "'s turn");

        setAvailableActions();
    }

    @Override
    public void showUpdateSuspendedGame() {
        System.out.println("\u001B[1m SUSPENDED GAME \u001B[0m\n");
    }

    @Override
    public void showWinners() {
        List<String> winners = new ArrayList<>();
        int maxPointsReached = 0;

        for (ClientPlayer i : controller.getPlayers()) {
            if (i.getPlayground().getPoints() > maxPointsReached) {
                maxPointsReached = i.getPlayground().getPoints();
                winners.clear();
                winners.add(i.getUsername());
            } else if (i.getPlayground().getPoints() == maxPointsReached) {
                winners.add(i.getUsername());
            }
        }

        System.out.println("\u001B[1m\u001B[5m" + "Winners\n" + "\u001B[0m\u001B[5m");
        for (String i : winners) {
            System.out.println("\u001B[3m" + i + "\u001B[0m\n");
        }

    }

}
