package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientRMI extends UnicastRemoteObject implements VirtualView {

    private final VirtualServer server;

    private ClientPlayer player;

    private ClientPhase currentPhase;

    private List<ClientPlayer> otherPlayers;

    private int[] hiddenObjectivesID; //max 2 and cannot be into ClientPlayer class because it would be visible to all the game partecipants

    private ClientBoard clientBoard;

    protected ClientRMI(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String serverName = "ServerRmi";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234); //todo change port and args and decide how to handle the exception

        VirtualServer server = (VirtualServer) registry.lookup(serverName);

        new ClientRMI(server).run();
    }


    //todo: this method assume the client tries to connect to the server till the server is available
    private void run() {
        while (true) {
            try {
                this.server.connect(this);
                break;
            } catch (RemoteException e) {
                System.out.println("Connection Error");
            }
        }
        this.readClientCommand();
    }

    private void readClientCommand() {

        Scanner scanner = new Scanner(System.in);
        boolean isEnded = false;

        //
        while (!isEnded) {
            switch (currentPhase) {
                case SETUP:
                    runSetUpPhase();
                case WAIT:
                    if(this.player.isCurrentPlayer()){
                        this.currentPhase = ClientPhase.NORMAL_TURN;
                    }
                case NORMAL_TURN:
                    runPlayerTurn();
                case ADDITIONAL_WAIT:
                    if(this.player.isCurrentPlayer()){
                        this.currentPhase = ClientPhase.ADDITIONAL_TURN;
                    }
                case ADDITIONAL_TURN:
                     runPlayerAdditionalTurn();
                case END:
                    //todo run end of the game
                    isEnded = true;
            }

        }
    }

    //todo check if ClientPlayground it's correctly updated, it should be updated by the methods from observer pattern
    //todo the server needs to send the map of corner

    private void runSetUpPhase(){

    }

    private void runPlayerAdditionalTurn(){
        receivePlaceCommand();
        receiveDrawCommand();
        this.player.setIsCurrentPlayer(false);
    }

    private void runPlayerTurn(){
        //while(!this.player.isCurrentPlayer()){}
        receivePlaceCommand();
        receiveDrawCommand();
        this.player.setIsCurrentPlayer(false);
    }

    private void receivePlaceCommand() {

        int numCard;
        Side selectedSide;
        Position position;

        while(true){
            numCard = receivePlayerCardPosition();
            try{
                selectedSide = receiveCardSide();
                if(selectedSide == Side.FRONT) {
                    try {
                        checkRequirements(this.player.getPlayerCardsFrontID()[numCard]);
                        position = receivePosition();
                        break;
                    }catch(Playground.NotEnoughResourcesException e){
                        System.out.println(e.getMessage());
                    }
                }

            }catch(ChangeCard ignored){
            }
        }

        try {
            server.placeCard(this.player.getUsername(), player.getPlayerCardsBackID()[numCard], player.getPlayerCardsFrontID()[numCard], selectedSide,position);
        } catch (Playground.UnavailablePositionException | Playground.NotEnoughResourcesException e) {
            System.err.println("Error check methods should have avoid an incorrect move");
        } catch (InvalidPlayerActionException | SuspendedGameException | InvalidGamePhaseException e) {
            System.err.println("Error");
        }
    }

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
            try{
                checkPosition(position, this.player.getPlayground());
                scanner.close();
                return position;
            }catch (Playground.UnavailablePositionException e){
                System.out.println(e.getMessage());
            }
        }
    }


    private void receiveDrawCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the card position");
        int cardPosition = scanner.nextInt();
        try {
            server.draw(this.player.getUsername(), cardPosition);
        } catch (InvalidPlayerActionException | EmptyDeckException | InvalidGamePhaseException e) {
            System.out.println("Error");
        }
    }

    private void checkPosition(Position position, ClientPlayground playground) throws Playground.UnavailablePositionException {
        if(!playground.getAvailablePositions().contains(position)){
            throw new Playground.UnavailablePositionException("The position insert isn't available");
        }
    }

    private void checkRequirements(int frontID) throws Playground.NotEnoughResourcesException{
        if(this.player.isGoldenFront(frontID)){
            for(Symbol s : this.player.getGoldenFrontRequirements(frontID).keySet()){
                if(this.player.getAmountResource(s) < this.player.getGoldenFrontRequirements(frontID).get(s)){
                    throw new Playground.NotEnoughResourcesException("You don't have the resources to place this side of the card");
                }
            }
        }
    }

    @Override
    public void showPlayerUsername(String username) throws RemoteException {
        System.out.println(player.getUsername());
    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        player.setNetworkStatus(isConnected);
        System.out.println(player.isConnected());
    }

    @Override
    public void showUpdatePlayerObjectiveCard(int[] privateObjectiveID, int starterCardID, String username) throws RemoteException {
        this.hiddenObjectivesID=privateObjectiveID.clone();
        player.setStarterFrontID(starterCardID);
        System.out.println("Hidden Objectives ID are: \n");
        for(int i:hiddenObjectivesID){
            System.out.println(i+"\n");
        }
        System.out.println("Starter card is: "+player.getStarterFrontID()+"\n");
        System.out.println("The username is: "+username);
    }

    @Override
    public void showBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException {
        clientBoard.setCommonObjectives(commonObjectiveID);
        clientBoard.addGoldenBackID(topGoldenBackID);
        clientBoard.addResourceBackID(topBackID);
        clientBoard.setFaceUpCards(faceUpCards);
        System.out.println("Common objectives are: \n");
        for (int i:clientBoard.getCommonObjectives()){
            System.out.println(i+"\n");
        }
        System.out.println("The top back ID is: "+ clientBoard.getResourceDeck().getLast() +"\n");
        System.out.println("The top golden back ID is: "+ clientBoard.getGoldenDeck().getLast() +"\n");
        System.out.println("Face up cards are: \n");
        for(int i:clientBoard.getFaceUpCards()){
            System.out.println(i+"\n");
        }
    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        player.setColor(color);
        System.out.println("The color is: "+player.getColor().name());
    }

    @Override
    public void showUpdateAfterPlace(List<Position> positions, List<ClientTile> tiles, List<Symbol> symbols, int[] totalAmount, int points, String username) throws RemoteException {
        if (positions.size() == tiles.size()) {//this can be omitted
            Iterator<Position> posIterator = positions.iterator();
            Iterator<ClientTile> tilesIterator = tiles.iterator();
            while (posIterator.hasNext() && tilesIterator.hasNext()) {
                player.getPlayground().placeTile(posIterator.next(), tilesIterator.next());
            }
        }
        if (symbols.size()==totalAmount.length){
            for(int i=0;i< symbols.size();i++){
                player.getPlayground().updateResources(symbols.get(i),totalAmount[i]);
            }
        }
        player.getPlayground().setPoints(points);
        System.out.println("Playground after place: \n"+player.getPlayground().toString());
    }

    @Override
    public void showUpdateAfterDraw(int newBackID, int newFrontID, Map<Symbol, Integer> goldenFrontRequirements, int cardHandPosition, boolean isEmpty, int newDeckBackID, int deckType, int newFrontFaceUp, int newBackFaceUp, int positionFaceUp, String Username) throws RemoteException {
        player.addPlayerCard(newBackID, newFrontID, cardHandPosition);
        if (!goldenFrontRequirements.isEmpty()) {
            player.addGoldenFrontRequirements(newFrontID, goldenFrontRequirements);
            System.out.println("Golden Front Requirements are: \n");
            for (Map.Entry<Symbol, Integer> e : player.getGoldenFrontRequirements(newFrontID).entrySet()) {
                System.out.println(e.getKey() + " --> " + e.getValue() + "\n");
            }
        } else {//if goldenFrontRequirements map is empty, it means that the drawn card isn't golden
            System.out.println("The card drawn doesn't have golden front requirements");
        }

        if (!isEmpty) {// 4 for normal deck, 5 for golden deck
            if (deckType == 4) {
                clientBoard.addResourceBackID(newDeckBackID);
                System.out.println("Top back ID in normal deck is: " + clientBoard.getResourceDeck().getLast() + "\n");
            } else if (deckType == 5) {
                clientBoard.addGoldenBackID(newBackID);
                System.out.println("Top back ID in golden deck is: " + clientBoard.getGoldenDeck().getLast() + "\n");
            }
        } else {
            System.out.println("The deck is empty \n");
        }

        if (positionFaceUp >= 0 && positionFaceUp <= 4) {
            clientBoard.addFaceUpCards(newFrontFaceUp, newBackFaceUp, positionFaceUp);
            System.out.println("Face up cards are: \n");
            for (int i : clientBoard.getFaceUpCards()) {
                System.out.println(i + "\n");
            }
        } else {
            System.out.println("Face up position is not valid \n");
        }

    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        System.out.println(message.getContent());
    }

    @Override
    public void showUpdateCurrentPlayer(ClientPlayer currentPlayer, ClientPhase phase) throws RemoteException {
        this.currentPhase=phase;
        this.player=currentPlayer;
        player.setIsCurrentPlayer(true);

        System.out.println(this.currentPhase.name());
        System.out.println(player.isCurrentPlayer()?player.getUsername():"Error");
    }

    @Override
    public void reportError(String details) throws RemoteException {
        System.out.println(details);
    }

    public static class ChangeCard extends Exception {
    }

}
