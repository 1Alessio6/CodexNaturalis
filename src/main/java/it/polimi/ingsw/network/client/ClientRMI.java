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
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ClientRMI extends UnicastRemoteObject implements VirtualView {

    private final VirtualServer server;

    private ClientPlayer player;

    private List<ClientPlayer> otherPlayers;

    private int[] hiddenObjectivesID; //max 2 and cannot be into ClientPlayer class because it would be visible to all the game partecipants

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

        while (true) {
            System.out.println("Insert a command:\n> ");
            String command = scanner.nextLine();

            switch (command) {
                case "place": //todo always add a way to go back to the card selection
                    receivePlaceCommand();




                case "end":
                    scanner.close();
                    break;
            }

        }
    }

    //todo check if ClientPlayground it's correctly updated, it should be updated by the methods from observer pattern
    //todo the server needs to send the map of corner

    private void runSetUpPhase(){

    }

    private void runPlayerTurn(){
        while(!this.player.isCurrentPlayer()){}
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
            System.out.println("Error check methods should have avoid an incorrect move");
        } catch (InvalidPlayerActionException | SuspendedGameException | InvalidGamePhaseException e) {
            System.out.println("Error");
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
    public void showColor(PlayerColor color, String username) throws RemoteException {
        player.setColor(color);
        System.out.println(player.getColor());
    }

    @Override
    public void showRemainingColor(Set<PlayerColor> remainingColor) throws RemoteException {
        remainingColor.forEach(System.out::println);
    }

    @Override
    public void showUpdatePlaygroundArea(Position position, ClientTile tile, String username) throws RemoteException {

    }

    @Override
    public void showUpdatePoints(int points, String username) throws RemoteException {
        player.getPlayground().setPoints(points);
        System.out.println(player.getPlayground().getPoints());
    }//hp: th passed points are the updated points

    @Override
    public void showUpdateResources(Symbol symbol, int totalAmount, String username) throws RemoteException {
        player.getPlayground().updateResources(symbol, totalAmount);
        System.out.println(player.getPlayground().getResources());
    }

    @Override
    public void showRemovePlayerCard(int backID, int frontID, int cardPosition, String Username) {

    }

    @Override
    public void showAddPlayerCard(int backID, int frontID, int cardPosition, String Username) {

    }

    @Override
    public void showUpdateDeck(boolean isEmpty, int backID) throws RemoteException {

    }

    @Override
    public void showUpdateFaceUpCards(int position, int backID, int frontID) throws RemoteException {

    }

    @Override
    public void showCommonObjectiveCard(int[] commonObjective) throws RemoteException {

    }

    @Override
    public void showUpdatePlayerObjectiveCard(int[] privateObjective) throws RemoteException {
        for (int i : hiddenObjectivesID) {
            System.out.println(i);
        }
    }

    @Override
    public void showPlayerStarterCard(int starterBackID, int starterFrontID, String username) throws RemoteException {
        player.setStarterBackID(starterBackID);
        player.setStarterFrontID(starterFrontID);
        System.out.println(player.getStarterFrontID() + player.getStarterBackID());
    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        System.out.println(message.getContent());
    }

    @Override
    public void showUpdateCurrentPlayer(Player currentPlayer) throws RemoteException {
        System.out.print(currentPlayer.getUsername());
    }

    @Override
    public void showUpdateGamePhase(String GamePhase) throws RemoteException {
        System.out.println(GamePhase);
    }

    @Override
    public void reportError(String details) throws RemoteException {
        System.out.println(details);
    }

    public static class ChangeCard extends Exception {
    }

}
