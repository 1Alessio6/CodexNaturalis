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
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.View;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ClientTUI implements View {
    private final Scanner console;
    private Thread thread;
    private Set<GameCommands> availableCommands = new HashSet<>();

    private final ClientController controller;

    public ClientTUI(ClientController controller) {
        this.controller = controller;
        this.console = new Scanner(System.in);

        availableCommands.add(GameCommands.HELP);
        availableCommands.add(GameCommands.QUIT);
    }

    private void parseGameCommands() {
        // TODO: command condition should be changed
        while (console.hasNextLine()) {
            // split command with spaces and analyze the first word
            String[] nextCommand = console.nextLine().toLowerCase().split(" ", 2);

            try {
                if (availableCommands.contains(GameCommands.valueOf(nextCommand[0].toUpperCase()))) {
                    switch (GameCommands.valueOf(nextCommand[0].toUpperCase())) {
                        case COLOR -> chooseColor();
                        case HELP -> ClientUtil.printHelpCommands(availableCommands);
                        case M, PM -> sendMessage(nextCommand);
                        case DRAW -> draw();
                        case PLACE -> place();
                        case QUIT -> quit();
                        case LOBBYSIZE -> setupLobbyPlayerNumber(Integer.parseInt(nextCommand[1]));
                        case OBJECTIVE -> chooseObjective();
                        case STARTER -> placeStarter();
                    }
                } else {
                    System.out.println("Invalid game command");
                    // print help for consented commands
                    ClientUtil.printHelpCommands(availableCommands);
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid game command");
                // print help for consented commands
                ClientUtil.printHelpCommands(availableCommands);
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
            int objectiveIdx = console.nextInt(); // starting from 1
            controller.placeObjectiveCard(objectiveIdx - 1);
        } catch (RemoteException | InvalidGamePhaseException | SuspendedGameException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setupLobbyPlayerNumber(int size) {
        try {
            controller.setPlayersNumber(size);
            // remove manually: only creator has this command
            availableCommands.remove(GameCommands.LOBBYSIZE);
        } catch (InvalidPlayersNumberException | RemoteException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    private void chooseColor() {
        try {
            controller.chooseColor(receiveColor());
        } catch (InvalidColorException | RemoteException | InvalidGamePhaseException | SuspendedGameException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private PlayerColor receiveColor() throws IllegalArgumentException {
        return PlayerColor.valueOf(console.nextLine());
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
        try {
            int drawFromId = getDrawPosition();
            controller.draw(drawFromId);
        } catch (InvalidIdForDrawingException | EmptyDeckException | NotExistingFaceUp | RemoteException |
                 InvalidGamePhaseException | SuspendedGameException e) {
            System.out.println(e.getMessage());
        }
    }

    private int getDrawPosition() {
        System.out.print("Insert the position of the card you want to draw: ");
        return console.nextInt();
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
        System.out.print("What side of the card you want to place, front or left?");
        return Side.valueOf(console.nextLine().toUpperCase());
    }

    private int receivePlayerCardPosition() {
        System.out.print("Enter card position in your hand: ");
        int cardPosition = console.nextInt();

        if(cardPosition >= 1 && cardPosition <= 3) return cardPosition;
        // todo: change exception
        else throw new RuntimeException("not a valid card position");
    }

    private Position receivePosition() {
        System.out.print("Insert position, with x and y space separated (e.g.: 1 2): ");
        int x = console.nextInt();
        int y = console.nextInt();
        return new Position(x, y);
    }

    /**
     * This method is invoked in a new thread at the beginning of a game
     * Commands can't be interrupted
     */
    private void beginCommandAcquisition() {
        // todo: synchronize to have correct command list
        new Thread(this::parseGameCommands).start();
    }

    @Override
    public void run(VirtualView client) {
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
        beginCommandAcquisition();
    }

    private void setAvailableCommands() {
        Set<GameCommands> availableCommands = new HashSet<>();
        availableCommands.add(GameCommands.HELP);
        availableCommands.add(GameCommands.QUIT);

        if (controller.getGamePhase() != null) {
            availableCommands.add(GameCommands.M);
            availableCommands.add(GameCommands.PM);
        }

        switch (controller.getGamePhase()) {
            case DrawNormal -> availableCommands.add(GameCommands.DRAW);
            case Setup -> availableCommands.add(GameCommands.STARTER);
            case PlaceNormal, PlaceAdditional -> availableCommands.add(GameCommands.PLACE);
        }

        this.availableCommands = availableCommands;
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
        availableCommands.add(GameCommands.LOBBYSIZE);
    }

    @Override
    public void showUpdateAfterLobbyCrash() {
        System.out.println("Lobby crashed! You will be disconnected. Please restart the client...");
    }

    @Override
    public void showUpdateAfterConnection() {
        System.out.println("Game is starting: you just joined");

        setAvailableCommands();
    }

    @Override
    public void showUpdatePlayerStatus() {
        List<String> usernames = controller.getConnectedUsernames();
        System.out.println("Connected players: <" + String.join(",", usernames) + ">");
    }

    @Override
    public void showInitialPlayerStatus() {

    }

    @Override
    public void showBoardSetUp() {
        StringBuilder str = new StringBuilder();
        int maxString = "Players".length() + 2;
        for (ClientPlayer i : controller.getPlayers()) {
            if (i.getUsername().length() >= maxString) {
                maxString = i.getUsername().length() + 2;
            }
        }

        String playerSpaces = ClientUtil.calculateSpaces(maxString, "Player");
        String pointsSpaces = ClientUtil.calculateSpaces(10, "Points");

        str.append("╔").append(ClientUtil.appendLine(maxString)).append("╦").append(ClientUtil.appendLine(10)).append("╗\n");
        str.append("║").append(playerSpaces).append("Player").append(playerSpaces).append("║");
        str.append(pointsSpaces).append("Points").append(pointsSpaces).append("║\n");
        str.append("╠").append(ClientUtil.appendLine(maxString)).append("╬").append(ClientUtil.appendLine(10)).append("╣\n");

        for (ClientPlayer i : controller.getPlayers()) {
            String username = i.getUsername();
            ANSIColor color = ClientUtil.playerColorConversion(i.getColor());
            int points = i.getPlayground().getPoints();

            str.append("║").append(color.getColor()).append(ClientUtil.calculateSpaces(maxString, username)).append(username).append(ClientUtil.calculateSpaces(maxString, username)).append(ANSIColor.RESET.getColor()).append("║");
            str.append(ClientUtil.calculateSpaces(10, Integer.toString(points))).append(points).append(ClientUtil.calculateSpaces(10, Integer.toString(points))).append("║\n");
        }
        str.append("╚").append(ClientUtil.appendLine(maxString)).append("╩").append(ClientUtil.appendLine(10)).append("╝\n");
        System.out.println(str);
    }

    @Override
    public void showStarterPlacement() {
        ClientUtil.printCard(controller.getMainPlayer().getStarterCard());
        setAvailableCommands();
    }

    @Override
    public void showUpdateColor() { //showUpdateColor shows the new scoreBoard with the updated colors
        showBoardSetUp();
        setAvailableCommands();
    }

    @Override
    public void showUpdateObjectiveCard() {
        ClientUtil.printObjectiveCard(controller.getMainPlayer().getObjectiveCards().getFirst());
        setAvailableCommands();
    }

    @Override
    public void showUpdateAfterPlace() {

        setAvailableCommands();
    }

    @Override
    public void showUpdateAfterDraw() {

        setAvailableCommands();
    }

    @Override
    public void showUpdateChat() {
        System.out.println(controller.getLastMessage());
    }

    @Override
    public void showUpdateCurrentPlayer() {
        System.out.println("\u001B[1mPlayer: \u001B[0m\n" + this.controller.getMainPlayerUsername());
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
