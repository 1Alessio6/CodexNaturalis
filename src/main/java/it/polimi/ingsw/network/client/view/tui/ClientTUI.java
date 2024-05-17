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
import it.polimi.ingsw.network.client.view.View;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientTUI implements View {
    private final Scanner console;
    private Thread thread;

    private final ClientController controller;

    public ClientTUI(ClientController controller) {
        this.controller = controller;
        this.console = new Scanner(System.in);
    }

    private void parseGameCommands() {
        // TODO: command condition should be changed
        while (console.hasNextLine()) {
            // split command with spaces and analyze the first word
            String[] nextCommand = console.nextLine().split(" ", 2);

            switch (nextCommand[0].toLowerCase()) {
                case "color" -> chooseColor();
                case "draw" -> draw();
                case "help" -> ClientUtil.gameActionsHelper();
                case "objective" -> chooseObjective();
                case "place" -> place();
                case "/pm","/m" -> sendMessage(nextCommand);
                case "quit" -> quit();
                case "" -> {}
                default -> {
                    System.out.println("Game action invalid");
                    ClientUtil.gameActionsHelper();
                }
            }
        }
    }

    private void parseLobbyCommands() {
        // TODO: command condition should be changed
        while (console.hasNextLine()) {
            // split command with spaces and analyze the first word
            String[] nextCommand = console.nextLine().split(" ", 2);

            switch (nextCommand[0].toLowerCase()) {
                case "help" -> ClientUtil.gameActionsHelper();
                case "quit" -> quit();
                case "lobbysize" -> setupLobbyPlayerNumber(Integer.parseInt(nextCommand[1]));
                case "" -> {}
                default -> {
                    System.out.println("Action invalid");
                    ClientUtil.gameActionsHelper();
                }
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

    private void setParseLogic(){
        if (controller.getGamePhase() == null) {
            parseLobbyCommands(); //phase does not exist
        } else {
            parseGameCommands();
        }
    }

    private void sendMessage(String[] command) {
        String commandContent = command[1];
        Message myMessage = command[0].equals("/pm") ? createPrivateMessage(commandContent) : createBroadcastMessage(commandContent);
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
        thread = new Thread(this::setParseLogic);
        thread.start();
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
    }

    @Override
    public void showUpdateAfterLobbyCrash() {
        System.out.println("Lobby crashed! You will be disconnected. Please restart the client...");
    }

    @Override
    public void showUpdateAfterConnection() {
        System.out.println("Game is starting: you just joined");

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

    }

    @Override
    public void showStarterPlacement() {

    }

    @Override
    public void showUpdateColor() {

    }

    @Override
    public void showUpdateObjectiveCard() {

    }

    @Override
    public void showUpdateAfterPlace() {

    }

    @Override
    public void showUpdateAfterDraw() {

    }

    @Override
    public void showUpdateChat() {

    }

    @Override
    public void showUpdateCurrentPlayer() {

    }

    @Override
    public void showUpdateSuspendedGame() {

    }

    @Override
    public void showWinners() {

    }
}
