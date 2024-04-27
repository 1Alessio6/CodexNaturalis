package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.Message;
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
    protected ClientRmi(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String serverName = "ServerRmi";
        Registry registry = LocateRegistry.getRegistry(args[0],1234); //todo change port and args and decide how to handle the exception

        VirtualServer server = (VirtualServer)registry.lookup(serverName);

        new ClientRmi(server).run();
    }


    //todo: this method assume the client tries to connect to the server till the server is available
    private void run(){
        while(true) {
            try {
                this.server.connect(this);
                break;
            } catch (RemoteException e) {
                System.out.println("Connection Error");
            }
        }
        this.readClientCommand();
    }

    private void readClientCommand(){

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("Insert a command:\n> ");
            String command = scanner.nextLine();
            if(command.equals("end")){
                scanner.close();
                break;
            }

        }
    }

    @Override
    public void showPlayerUsername(String username) throws RemoteException {

    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected) throws RemoteException {

    }

    @Override
    public void showColor(PlayerColor color) throws RemoteException {

    }

    @Override
    public void showRemainingColor(Set<PlayerColor> remainingColor) throws RemoteException {

    }

    @Override
    public void showUpdatePlaygroundArea(Position position, Tile tile) throws RemoteException {

    }

    @Override
    public void showUpdatePoints(int points) throws RemoteException {

    }

    @Override
    public void showUpdateResources(Symbol symbol, int totalAmount) throws RemoteException {

    }

    @Override
    public void showUpdatePlayerCards(List<Card> newCards) throws RemoteException {

    }

    @Override
    public void showUpdateDeck(boolean isEmpty) throws RemoteException {

    }

    @Override
    public void showUpdateFaceUpCards(int position, Card card) throws RemoteException {

    }

    @Override
    public void showCommonObjectiveCard(List<ObjectiveCard> commonObjective) throws RemoteException {

    }

    @Override
    public void showUpdatePlayerObjectiveCard(List<ObjectiveCard> privateObjective) throws RemoteException {

    }

    @Override
    public void showPlayerStarterCard(Card starterCard) throws RemoteException {

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
}
