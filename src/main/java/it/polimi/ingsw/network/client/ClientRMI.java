package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.NotAvailableUsername;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.*;
import it.polimi.ingsw.network.client.model.card.*;
import it.polimi.ingsw.network.client.model.player.*;
import it.polimi.ingsw.network.client.model.board.*;
import it.polimi.ingsw.network.client.view.View;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientRMI extends UnicastRemoteObject implements VirtualView {

    private VirtualServer server;

    private ClientGame game;

    private View clientView; //can be tui or gui

    public ClientRMI() throws RemoteException{

    }

    public ClientRMI(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String serverName = "ServerRmi";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234); //todo change port and args and decide how to handle the exception

        VirtualServer server = (VirtualServer) registry.lookup(serverName);

        new ClientRMI(server).run();
    }


    //this method assume the client tries to connect to the server till the server is available
    private void run() {
        connect();
        this.readClientCommand();
    }

    private void readClientCommand() {

        /*
        Scanner scanner = new Scanner(System.in);
        boolean isEnded = false;


        while (!isEnded) {
            switch (game.getCurrentPhase()) {
                case SETUP:
                    runSetUpPhase();
                case WAIT:
                    if (this.game.getMainPlayer().isCurrentPlayer()) {
                        this.game.setCurrentPhase(ClientPhase.NORMAL_TURN);
                    }
                case NORMAL_TURN:
                    runPlayerTurn();
                case ADDITIONAL_WAIT:
                    if (this.game.getMainPlayer().isCurrentPlayer()) {
                        this.game.setCurrentPhase(ClientPhase.ADDITIONAL_TURN);
                    }
                case ADDITIONAL_TURN:
                    runPlayerAdditionalTurn();
                case END:
                    //todo run end of the game
                    isEnded = true;
            }

        }

         */
    }

    //todo check if ClientPlayground it's correctly updated, it should be updated by the methods from observer pattern
    //todo the server needs to send the map of corner

    private void connect(){
        while(true) {
            try {
                String username = receiveUsername();
                this.server.connect(this, username);
                break;
            }catch(InvalidUsernameException e){
                System.err.println("Username selected is already taken. Please try again");
            } catch (RemoteException e) {
                System.err.println("Connection Error");
            } catch (FullLobbyException e){
                System.err.println("Error");
            }
        }
    }

    private String receiveUsername(){
        System.out.println("Insert username");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        scanner.close();

        return username;
    }

    private void runSetUpPhase() {

    }

    private void runPlayerAdditionalTurn() {
        receivePlaceCommand();
        receiveDrawCommand();
        this.game.setCurrentMainPlayer(false);
    }

    private void runPlayerTurn() {
        //while(!this.player.isCurrentPlayer()){}
        receivePlaceCommand();
        receiveDrawCommand();
        this.game.setCurrentMainPlayer(false);
    }

    private void receivePlaceCommand() {

        /*
        int numCard;
        Side selectedSide;
        Position position;

        while (true) {
            numCard = receivePlayerCardPosition();
            try {
                selectedSide = receiveCardSide();
                position = receivePosition();

            } catch (ChangeCard ignored) {
            }
        }

         */

    }


    /*
    private int receivePlayerCardPosition() {


        int numCard = -1;
        Scanner scanner = new Scanner(System.in);
        while (numCard < 0 || numCard > 2) {
            System.out.println("Which card from your hand do you select? { 0 1 2}");
            numCard = scanner.nextInt();
            if (numCard < 0 || numCard > 2) {
                System.out.println("You don't have that card in your hand");
            }
        }
        scanner.close();

        return numCard;


    }
     */

    private Side receiveCardSide() throws ChangeCard {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Which face of the card do you want to place?\nWrite 0 for FRONT or 1 for BACK, or write any other number to change the card selected");
        int selectedSide = scanner.nextInt();

        return switch (selectedSide) {
            case 0 -> {
                scanner.close();
                yield Side.FRONT;
            }
            case 1 -> {
                scanner.close();
                yield Side.BACK;
            }
            default -> {
                scanner.close();
                throw new ChangeCard();
            }
        };
    }

    private Position receivePosition() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Which position?");
            int posX = scanner.nextInt();
            int posY = scanner.nextInt();
            Position position = new Position(posX, posY);
            scanner.close();
        }
    }


    private void receiveDrawCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the card position");
        int cardPosition = scanner.nextInt();
        try {
            server.draw(this.game.getMainPlayerUsername(), cardPosition);
        } catch (InvalidPlayerActionException | EmptyDeckException | InvalidGamePhaseException e) {
            System.out.println("Error");
        }
    }

    @Override
    public void updateCreator() throws RemoteException {

    }

    @Override
    public void updateAfterLobbyCrash() throws RemoteException {

    }

    @Override
    public void updateAfterConnection(ClientGame clientGame) {

    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        game.getMainPlayer().setNetworkStatus(isConnected);
        System.out.println(game.getMainPlayer().isConnected());
    }

    @Override
    public void showInitialPlayerStatus(ClientPlayer player) throws RemoteException {

    }

    @Override
    public void showBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException {

        /*
        game.getClientBoard().setCommonObjectives(commonObjectiveID);
        game.getClientBoard().addGoldenBackID(topGoldenBackID);
        game.getClientBoard().addResourceBackID(topBackID);
        game.getClientBoard().setFaceUpCards(faceUpCards);
        System.out.println("Common objectives are: \n");
        for (int i : game.getClientBoard().getCommonObjectives()) {
            System.out.println(i + "\n");
        }
        System.out.println("The top back ID is: " + game.getClientBoard().getResourceDeck().getLast() + "\n");
        System.out.println("The top golden back ID is: " + game.getClientBoard().getGoldenDeck().getLast() + "\n");
        System.out.println("Face up cards are: \n");
        for (int i : game.getClientBoard().getFaceUpCards()) {
            System.out.println(i + "\n");
        }

         */
    }

    @Override
    public void showStarterPlacement(String username, int faceId) {

    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        for(ClientPlayer player : game.getPlayers()){
            if(player.getUsername().equals(username)){
                player.setColor(color);
            }
        }

    }

    //delete
    @Override
    public void showUpdateObjectiveCard(ClientCard chosenObjective, String username) {

    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Position position) throws RemoteException {

    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, boolean isEmpty, ClientCard newTopDeck, ClientCard newFaceUpCard, ClientCard newTopCard, boolean additionalTurn, String username, int boardPosition) throws RemoteException {


        game.getPlayer(username).addPlayerCard(drawnCard);

        /*
        if (!isEmpty) {// 4 for normal deck, 5 for golden deck
            if (deckType == 4) {
                game.clientBoard.addResourceBackID(newDeckBackID);
                System.out.println("Top back ID in normal deck is: " + game.clientBoard.getResourceDeck().getLast() + "\n");
            } else if (deckType == 5) {
                game.clientBoard.addGoldenBackID(newDeckBackID);
                System.out.println("Top back ID in golden deck is: " + game.clientBoard.getGoldenDeck().getLast() + "\n");
            }
            else{
                System.err.println("Error, this deck doesn't exist");
            }
        } else {
            System.out.println("The deck is empty \n");
        }

        if (positionFaceUp >= 0 && positionFaceUp <= 4) {
            game.clientBoard.addFaceUpCards(newFrontFaceUp, newBackFaceUp, positionFaceUp);
            System.out.println("Face up cards are: \n");
            for (int i : game.clientBoard.getFaceUpCards()) {
                System.out.println(i + "\n");
            }
        } else {
            System.out.println("Face up position is not valid \n");
        }

         */
    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        System.out.println(message.getContent());
    }

    @Override
    public void showUpdateCurrentPlayer(ClientPlayer currentPlayer, ClientPhase phase) throws RemoteException {
        /*
        this.game.currentPhase = phase;
        this.game.player = currentPlayer;
        game.player.setIsCurrentPlayer(true);

        System.out.println(this.game.currentPhase.name());
        System.out.println(game.player.isCurrentPlayer() ? game.player.getUsername() : "Error");

         */
    }

    @Override
    public void showUpdateSuspendedGame() throws RemoteException {

    }

    @Override
    public void showWinners(List<String> winners) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {
        System.err.println(details);
    }

    public static class ChangeCard extends Exception {
    }

}
