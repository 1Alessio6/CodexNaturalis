package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class PlainVirtualView implements VirtualView {
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCreator() throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterLobbyCrash() throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterConnection(ClientGame clientGame) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat(Message message) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateGameState() throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportError(String details) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) throws RemoteException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {

    }
}
