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
import it.polimi.ingsw.network.client.view.View;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientTUI extends View {
    private Scanner console;

    public ClientTUI() {
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
                case "quit" -> System.exit(0);
                case "" -> {}
                default -> {
                    System.out.println("Game action invalid");
                    ClientUtil.gameActionsHelper();
                }
            }
        }
    }

    private void chooseObjective() {
        System.out.println("Choose objective idx: ");
        try {
            int objectiveIdx = console.nextInt(); // starting from 1
            this.getController().placeObjectiveCard(objectiveIdx - 1);
        } catch (RemoteException | InvalidGamePhaseException | SuspendedGameException e) {
            System.out.println(e.getMessage());
        }
    }

    private void parseLobbyCommands() {
        // TODO: command condition should be changed
        while (console.hasNextLine()) {
            // split command with spaces and analyze the first word
            String[] nextCommand = console.nextLine().split(" ", 2);

            switch (nextCommand[0].toLowerCase()) {
                case "help" -> ClientUtil.gameActionsHelper();
                case "quit" -> System.exit(0);
                case "players" -> setupLobbyPlayerNumber();
                case "" -> {}
                default -> {
                    System.out.println("Action invalid");
                    ClientUtil.gameActionsHelper();
                }
            }
        }
    }

    private void setupLobbyPlayerNumber() {
        System.out.print("Setup lobby player number: ");
        try {
            this.getController().setPlayersNumber(console.nextInt());
        } catch (InvalidPlayersNumberException | RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    private void chooseColor() {
        try {
            this.getController().chooseColor(receiveColor());
        } catch (InvalidColorException | RemoteException | InvalidGamePhaseException | SuspendedGameException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private PlayerColor receiveColor() throws IllegalArgumentException {
        return PlayerColor.valueOf(console.nextLine());
    }

    private void setParseLogic(){
        if (this.getController().getGamePhase() == null) {
            parseLobbyCommands(); //phase does not exist
        } else {
            parseGameCommands();
        }
    }

    private void sendMessage(String[] command) {
        String commandContent = command[1];
        Message myMessage = command[0].equals("/pm") ? createPrivateMessage(commandContent) : createBroadcastMessage(commandContent);
        try {
            this.getController().sendMessage(myMessage);
        } catch (InvalidMessageException | RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    private Message createPrivateMessage(String messageDetails) {
        // messageDetails contains recipient too
        String[] messageSplit = messageDetails.split(" ", 2);
        String recipient = messageSplit[0];
        String messageContent = messageSplit[1];
        return new Message(this.getController().getMainPlayerUsername(), recipient, messageContent);
    }

    private Message createBroadcastMessage(String messageContent) {
        return new Message(this.getController().getMainPlayerUsername(), messageContent);
    }

    private void draw() {
        try {
            int drawFromId = getDrawPosition();
            this.getController().draw(drawFromId);
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
            this.getController().placeCard(receivePlayerCardPosition(), receiveSide(), receivePosition());
        } catch (Playground.UnavailablePositionException | Playground.NotEnoughResourcesException | RemoteException |
                 InvalidGamePhaseException | SuspendedGameException e) {
            System.out.println(e.getMessage());
        }
    }

    private void placeStarter() {
        try {
            this.getController().placeStarter(receiveSide());
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
        new Thread(() -> {
            console = new Scanner(System.in);
            setParseLogic();
        }).start();
    }

    @Override
    public void run(VirtualView client) {
        // connection logic
        while(true) {
            try {
                this.getController().connect(client, receiveUsername());
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

    }

    @Override
    public void showUpdateCreator() {

    }

    @Override
    public void showUpdateAfterLobbyCrash() {

    }

    @Override
    public void showUpdateAfterConnection() {

    }

    @Override
    public void showUpdatePlayerStatus() {

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
