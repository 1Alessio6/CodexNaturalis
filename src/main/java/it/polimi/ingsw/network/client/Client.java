package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.client.model.*;
import it.polimi.ingsw.network.client.model.card.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * The Client class represents the endpoint of a connection with the server.
 * It provides methods to update the model representation on the client side as well as their view.
 */
public abstract class Client implements VirtualView {
    protected ClientController controller;
    protected View clientView;
    protected boolean isConnected;
    protected final Object lockOnNetworkStatus = new Object();

    public Client() {
        isConnected = false;
    }

    /**
     * Connects the client to the <code>ip</code> and <code>port</code> provided.
     *
     * @param ip   the ip address.
     * @param port the port number.
     * @throws UnReachableServerException if the server isn't reachable.
     */
    public abstract VirtualServer bindServer(String ip, Integer port) throws UnReachableServerException;

    /**
     * Returns the instance of the client for the server to communicate updates.
     *
     * @return a VirtualView representing the client reference for the server.
     * @throws RemoteException if it's impossible to obtain the reference for the server because of network problems.
     */
    public abstract VirtualView getInstanceForTheServer() throws RemoteException;


    /**
     * Returns the state of the connection between the client and the server
     *
     * @return true if <code>this</code> client is connected to the server; false otherwise.
     */
    public boolean isConnected() {
        synchronized (lockOnNetworkStatus) {
            return isConnected;
        }
    }

    /**
     * Adds the controller and the view for the client
     *
     * @param controller to communicate modifications of the game.
     * @param view       to show updates to the screen.
     */
    public void configure(ClientController controller, View view) {
        this.controller = controller;
        this.clientView = view;
    }


    public ClientController getController() {
        return controller;
    }

    /**
     * Runs the view.
     *
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    public void runView() throws RemoteException {
        clientView.runView();
    }

    public void handleServerCrash() {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                terminateConnection();
                clientView.showServerCrash();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCreator() throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                clientView.showUpdateCreator();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterConnection(ClientGame clientGame) {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                System.out.println("Received updateAfterConnection which belongs to me");
                controller.updateAfterConnection(clientGame);
                clientView.showUpdateAfterConnection();
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                controller.updatePlayersInLobby(usernames);
                clientView.showUpdatePlayersInLobby();
            }
        }
    }

    /**
     * Closes all references to the server.
     */
    protected abstract void terminateConnection();

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterLobbyCrash() throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                terminateConnection();
                clientView.showUpdateAfterLobbyCrash();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateExceedingPlayer() throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                terminateConnection();
                clientView.showUpdateExceedingPlayer();
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (this.isConnected) {
                controller.updatePlayerStatus(isConnected, username);
                clientView.showUpdatePlayerStatus();
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                controller.updateColor(color, username);
                clientView.showUpdateColor(username);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                controller.updateObjectiveCard(chosenObjective, username);
                //todo: if the objective card isn't of the main player the view should not show the card
                if (controller.getMainPlayerUsername().equals(username)) {
                    clientView.showUpdateObjectiveCard();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                controller.updateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);

                if (controller.getGamePhase().equals(GamePhase.Setup)) {
                    clientView.showStarterPlacement(username);
                } else {
                    clientView.showUpdateAfterPlace(username);
                }
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                controller.updateAfterDraw(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
                clientView.showUpdateAfterDraw(username);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                controller.updateChat(message);
                clientView.showUpdateChat();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                controller.updateCurrentPlayer(currentPlayerIdx, phase);
                clientView.showUpdateCurrentPlayer();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateGameState() throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                controller.updateSuspendedGame();
                clientView.showUpdateSuspendedGame();
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) throws RemoteException {
        System.err.print("Winners: ");
        winners.forEach(System.err::println);

        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                clientView.showWinners(winners);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportError(String details) throws RemoteException {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                clientView.reportError(details);
            }
        }
    }

}