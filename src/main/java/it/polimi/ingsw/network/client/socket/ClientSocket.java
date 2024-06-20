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
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.heartbeat.HeartBeat;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * The ClientSocket updates the view content and the information present in the ClientController when Socket
 * communication is used.
 */
public class ClientSocket extends Client implements VirtualView, HeartBeatHandler {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private HeartBeat heartBeat;

    public ClientSocket() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void runView() {
        clientView.runView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void disconnect() {
        heartBeat.terminate();
        closeResources();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualView getInstanceForTheServer() {
        return this;
    }

    /**
     * Closes input, output and socket.
     */
    private void closeResources() {
        try {
            this.socket.close();
            this.out.close();
            this.in.close();
        } catch (Exception ignored) {

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualServer bindServer(String ip, Integer port) throws UnReachableServerException {
        //System.out.println("Trying to connect to ip " + ip + " at port " + port);
        try {
            this.socket = new Socket(ip, port);
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            VirtualServer server = new ServerHandler(this, in, out);

            heartBeat = new HeartBeat(this, "unknown", server, "server");
            heartBeat.startHeartBeat();
            new Thread(() -> {
                try {
                    server.hear();
                } catch (RemoteException ignored) {
                    // impossible from a socket server
                }
            }).start();
            return server;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + ip);
            closeResources();
            throw new UnReachableServerException(e.getMessage());
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + ip);
            closeResources();
            throw new UnReachableServerException(e.getMessage());
        }
    }

    //   public void terminateConnection() {
    //       try {
    //           in.close();
    //           out.close();
    //           socket.close();
    //       } catch (IOException ignored) {
    //       }
    //   }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCreator() {
        clientView.showUpdateCreator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterLobbyCrash() {
        clientView.showUpdateAfterLobbyCrash();
        disconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterConnection(ClientGame clientGame) {
        controller.updateAfterConnection(clientGame);
        clientView.showUpdateAfterConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) {
        controller.updatePlayersInLobby(usernames);
        clientView.showUpdatePlayersInLobby();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateFullLobby() {
        clientView.showUpdateFullLobby();
        disconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateExceedingPlayer() {
        clientView.showUpdateExceedingPlayer();
        disconnect();
    }

    //   @Override
    //   public void showUpdateJoinedPlayers(List<String> usernames) throws RemoteException {

    //   }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) {
        controller.updatePlayerStatus(isConnected, username);
        clientView.showUpdatePlayerStatus();
    }

    //   @Override
    //   public void showStarterPlacement(String username, int faceId) {

    //   }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(PlayerColor color, String username) {
        controller.updateColor(color, username);
        clientView.showUpdateColor(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        controller.updateObjectiveCard(chosenObjective, username);
        //todo: if the objective card isn't of the main player the view should not show the card
        if (controller.getMainPlayerUsername().equals(username)) {
            clientView.showUpdateObjectiveCard();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) {
        controller.updateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
        if (this.controller.getGamePhase().equals(GamePhase.Setup)) {
            clientView.showStarterPlacement(username);
        } else
            clientView.showUpdateAfterPlace(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) {
        controller.updateAfterDraw(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
        clientView.showUpdateAfterDraw(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat(Message message) {
        controller.updateChat(message);
        clientView.showUpdateChat();
    }

    //@Override
    //public void showUpdateTriggeredEndGame(String username) {

    //}

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) {
        controller.updateCurrentPlayer(currentPlayerIdx, phase);
        clientView.showUpdateCurrentPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateGameState() {
        controller.updateSuspendedGame();
        clientView.showUpdateSuspendedGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) {
        clientView.showWinners(winners);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportError(String details) {
        clientView.reportError(details);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resultOfLogin(boolean accepted, String username, String details) throws RemoteException {
        if (accepted) {
            controller.setMainPlayerUsername(username);
            heartBeat.setHandlerName(username);
           // heartBeat.startHeartBeat();
        } else {
            clientView.showInvalidLogin(details);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {
        if (heartBeat.isActive()) {
            clientView.showServerCrash();
            disconnect();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        heartBeat.registerMessage(ping);
    }
}
