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

public class ClientRmi extends UnicastRemoteObject implements VirtualView {

    private final VirtualServer server;

    private ClientPlayer player;

    private List<ClientPlayer> otherPlayers;

    private int[] hiddenObjectivesID; //max 2 and cannot be into ClientPlayer class because it would be visible to all the game partecipants

    protected ClientRmi(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String serverName = "ServerRmi";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234); //todo change port and args and decide how to handle the exception

        VirtualServer server = (VirtualServer) registry.lookup(serverName);

        new ClientRmi(server).run();
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

                    //todo check if ClientPlayground it's correctly updated, it should be updated by the methods from observer pattern
                    //todo the server needs to send the map of corner


                case "end":
                    scanner.close();
                    break;
            }

        }
    }

    private void receivePlaceCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which card do you select?");
        int numCard = scanner.nextInt();
        System.out.println("Which face of the card do you want to place?");
        Side selectedSide = convertSide(scanner.nextLine());
        System.out.println("Which position?");
        int posX = scanner.nextInt();
        int posY = scanner.nextInt();
        //todo add exception handling
        checkPosition();
        checkRequirements();
        try {
            server.placeCard(this.player.getUsername(), player.getBackID()[numCard], player.getFrontID()[numCard], selectedSide, new Position(posX, posY));
        } catch (Playground.UnavailablePositionException | Playground.NotEnoughResourcesException e) {
            System.out.println("Error check methods should have avoid an incorrect move");
        } catch (InvalidPlayerActionException | SuspendedGameException | InvalidGamePhaseException e) {
            System.out.println("Error");
        }
    }

    private Side convertSide(String s) {
        return Side.FRONT;
    }

    private void checkRequirements() {
    }

    @Override
    public void showPlayerUsername(String username) throws RemoteException {

    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {

    }

    @Override
    public void showColor(PlayerColor color, String username) throws RemoteException {

    }

    @Override
    public void showRemainingColor(Set<PlayerColor> remainingColor) throws RemoteException {

    }

    @Override
    public void showUpdatePlaygroundArea(Position position, ClientTile tile, String username) throws RemoteException {

    }

    @Override
    public void showUpdatePoints(int points, String username) throws RemoteException {

    }

    @Override
    public void showUpdateResources(Symbol symbol, int totalAmount, String username) throws RemoteException {

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
    public void showUpdateFaceUpCards(int position, Card card) throws RemoteException {

    }

    @Override
    public void showCommonObjectiveCard(int[] commonObjective) throws RemoteException {

    }

    @Override
    public void showUpdatePlayerObjectiveCard(int[] privateObjective) throws RemoteException {

    }

    @Override
    public void showPlayerStarterCard(int starterBackID, int starterFrontID, String username) throws RemoteException {

    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {

    }

    @Override
    public void showUpdateCurrentPlayer(Player currentPlayer) throws RemoteException {

    }

    @Override
    public void showUpdateGamePhase(String GamePhase) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {

    }

    private void checkPosition() {

    }
    public String getUsername(){
        return this.player.getUsername();
    }
}
