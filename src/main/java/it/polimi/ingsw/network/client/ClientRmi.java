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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;

public class ClientRmi extends UnicastRemoteObject implements VirtualView {

    private final VirtualServer server;
    protected ClientRmi(VirtualServer server) throws RemoteException {
        this.server = server;
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
