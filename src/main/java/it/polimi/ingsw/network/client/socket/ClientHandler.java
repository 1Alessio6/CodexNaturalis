package it.polimi.ingsw.network.client.socket;

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
import it.polimi.ingsw.network.server.socket.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientHandler implements VirtualView {

    private final Server server;
    private final PrintWriter out;
    private final BufferedReader input;

    public ClientHandler(Server server, BufferedReader input, PrintWriter out) {
        this.server = server;
        this.out = out;
        this.input = input;
    }

    public void run() throws IOException {
    }


    @Override
    public void updateCreator() throws RemoteException {

    }

    @Override
    public void updateAfterLobbyCrash() throws RemoteException {

    }

    @Override
    public void updateAfterConnection(ClientGame clientGame) throws RemoteException {

    }

    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {

    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {

    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {

    }

    @Override
    public void showUpdateObjectiveCard(ClientCard chosenObjective, String username) throws RemoteException {

    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {

    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {

    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {

    }

    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {

    }

    @Override
    public void showUpdateSuspendedGame() throws RemoteException {

    }

    @Override
    public void showWinners(List<String> winners) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {

    }


}
