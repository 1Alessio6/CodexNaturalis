package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.heartbeat.HeartBeat;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;
import it.polimi.ingsw.network.server.Server;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class RMIHandler implements ClientHandler, HeartBeatHandler {
    private final VirtualView stub;
    private final String username;
    private final HeartBeat heartBeat;
    private final Server server;
    private final ExecutorService notificationHandler;
    private final AtomicBoolean isActive;

    public RMIHandler(Server server, VirtualView stub, String username) {
        this.server = server;
        this.stub = stub;
        System.out.println("Is the stub null? " + (this.stub == null));
        assert this.stub != null;
        this.username = username;
        this.heartBeat = new HeartBeat(this, username + "_handler", stub, username);
        this.isActive = new AtomicBoolean(true);
        this.notificationHandler = Executors.newSingleThreadExecutor();
    }

    public String getUsername() {
        return username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startHeartBeat() {
        heartBeat.startHeartBeat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCreator() {
        notificationHandler.submit(() -> {
            System.out.println("Sending updateCreator");
            if (isActive.get()) {
                try {
                    stub.updateCreator();
                } catch (RemoteException ignored) {
                    System.err.println("Crashed while sending the updateCreator");
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterConnection(ClientGame clientGame) {
        notificationHandler.submit(() -> {
            System.out.println("Sending update after connection");
            if (isActive.get()) {
                try {
                    stub.updateAfterConnection(clientGame);
                } catch (RemoteException ignored) {
                    System.err.println("Crashed while sending updateAfterConnection");
                }
            } else {
                System.out.println("update after connection is outdated, now the handler is inactive");
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showUpdatePlayersInLobby(usernames);
                } catch (RemoteException ignored) {
                    System.err.println("Crashed while sending showUpdatePlayersInLobby");
                }
            }
        });
    }

    private void makeHandlerInvalid() {
        heartBeat.terminate();
        isActive.set(false);
        notificationHandler.shutdownNow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate() {
        makeHandlerInvalid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerPingFromClient(HeartBeatMessage ping) {
        heartBeat.registerMessage(ping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateExceedingPlayer() {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showUpdateExceedingPlayer();
                } catch (RemoteException ignored) {

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterLobbyCrash() {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.updateAfterLobbyCrash();
                } catch (RemoteException ignored) {

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showUpdatePlayerStatus(isConnected, username);
                } catch (RemoteException ignored) {

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(PlayerColor color, String username) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showUpdateColor(color, username);
                } catch (RemoteException ignored) {
                    System.err.println("Remote exception in showUpdateColor: " + ignored.getMessage());
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showUpdateObjectiveCard(chosenObjective, username);
                } catch (RemoteException ignored) {
                    System.err.println("Remote exception in showUpdateObjectiveCard: " + ignored.getMessage());
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                System.out.println("Sending update after place");
                try {
                    stub.showUpdateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
                } catch (RemoteException ignored) {
                    System.err.println("Remote exception in showUpdateAfterPlace" + ignored.getMessage());
                }
            } else {
                System.out.println("Update will be lost");
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showUpdateAfterDraw(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
                } catch (RemoteException ignored) {

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat(Message message) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showUpdateChat(message);
                } catch (RemoteException ignored) {

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showUpdateCurrentPlayer(currentPlayerIdx, phase);
                } catch (RemoteException ignored) {
                    System.err.println("Remote exception in showUpdateCurrentPlayer: " + ignored.getMessage());
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateGameState() {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showUpdateGameState();
                } catch (RemoteException ignored) {

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.showWinners(winners);
                } catch (RemoteException ignored) {

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportError(String details) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                try {
                    stub.reportError(details);
                } catch (RemoteException ignored) {

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resultOfLogin(boolean accepted, String details) {
        notificationHandler.submit(() -> {
            System.out.println("Sending resultOfLogin for " + username);
            if (isActive.get()) {
                try {
                    stub.resultOfLogin(accepted, username, details);
                } catch (RemoteException ignored) {

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        stub.receivePing(ping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {
        server.handleUnresponsiveness(unresponsiveListener);
    }
}
