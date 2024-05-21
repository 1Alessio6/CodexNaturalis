package it.polimi.ingsw.network.client.socket;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.heartbeat.HeartBeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientSocket extends Client implements VirtualView {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private HeartBeat heartBeat;
    private String name;

    public ClientSocket(String host, Integer port) throws UnReachableServerException {
        super(host, port);
        heartBeat = new HeartBeat(this, server);
    }

    @Override
    public void runView() {
        ClientController controller = clientView.run(this);
        name = controller.getMainPlayerUsername();
        clientView.beginCommandAcquisition();
        heartBeat.startPing(name);
    }

    @Override
    protected VirtualServer connect(String ip, Integer port) {
        System.out.println("Connect to ip " + ip + " at port " + port);
        ServerHandler serverHandler = null;

        try (
                Socket socket = new Socket(ip, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            serverHandler = new ServerHandler(this, in, out);
            this.socket = socket;
            this.out = out;
            this.in = in;
            new Thread(serverHandler::hear);
            return serverHandler;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + ip);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + ip);
        }
        return null;
    }

 //   public void terminateConnection() {
 //       try {
 //           in.close();
 //           out.close();
 //           socket.close();
 //       } catch (IOException ignored) {
 //       }
 //   }

    @Override
    public void updateCreator() {
        clientView.showUpdateCreator();
    }

    @Override
    public void updateAfterLobbyCrash() {
        clientView.showUpdateAfterLobbyCrash();
    }

    @Override
    public void updateAfterConnection(ClientGame clientGame) {
        controller.updateAfterConnection(clientGame);
        clientView.showUpdateAfterConnection();
    }

    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) {
        controller.updatePlayersInLobby(usernames);
        clientView.showUpdatePlayersInLobby();
    }

 //   @Override
 //   public void showUpdateJoinedPlayers(List<String> usernames) throws RemoteException {

 //   }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username){
        controller.updatePlayerStatus(isConnected, username);
        clientView.showUpdatePlayerStatus();
    }

 //   @Override
 //   public void showStarterPlacement(String username, int faceId) {

 //   }

    @Override
    public void showUpdateColor(PlayerColor color, String username) {
        controller.updateColor(color, username);
        clientView.showUpdateColor(username);
    }

    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        controller.updateObjectiveCard(chosenObjective, username);
        //todo: if the objective card isn't of the main player the view should not show the card
        if(controller.getMainPlayerUsername().equals(username)){
            clientView.showUpdateObjectiveCard();
        }
    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position){
        controller.updateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
        clientView.showUpdateAfterPlace();
    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) {
        controller.updateAfterDraw(drawnCard,newTopDeck,newFaceUpCard,username,boardPosition);
        clientView.showUpdateAfterDraw();
    }

    @Override
    public void showUpdateChat(Message message) {
        controller.updateChat(message);
        clientView.showUpdateChat();
    }

    //@Override
    //public void showUpdateTriggeredEndGame(String username) {

    //}

    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) {
        controller.updateCurrentPlayer(currentPlayerIdx,phase);
        clientView.showUpdateCurrentPlayer();
    }

    @Override
    public void showUpdateSuspendedGame() {
        controller.updateSuspendedGame();
        clientView.showUpdateSuspendedGame();
    }

    @Override
    public void showWinners(List<String> winners) {
        clientView.showWinners();
    }

    @Override
    public void reportError(String details) {
        System.err.println(details);
    }

    @Override
    public void notifyStillActive() throws RemoteException {
        heartBeat.registerResponse();
    }

    @Override
    public void handleUnresponsiveness() {
        clientView.showServerCrash();
        heartBeat.shutDown();
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
        }
        System.exit(1);
    }
}
