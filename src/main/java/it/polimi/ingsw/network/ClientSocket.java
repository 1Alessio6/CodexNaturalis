package it.polimi.ingsw.network;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.ClientPhase;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientSocket implements VirtualView{
    @Override
    public void updateCreator() throws RemoteException {

    }

    @Override
    public void updateAfterLobbyCrash() throws RemoteException {

    }

    @Override
    public void updateAfterConnection(ClientGame clientGame) {

    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {

    }

    @Override
    public void showBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) throws RemoteException {

    }

    @Override
    public void showStarterPlacement(String username, int faceId) {

    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {

    }

    @Override
    public void showUpdateObjectiveCard(ClientCard chosenObjective, String username) {

    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientFace placedface, Position position) throws RemoteException {

    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, boolean isEmpty, ClientCard newTopDeck, ClientCard newFaceUpCard, ClientCard newTopCard, boolean additionalTurn, String username, int boardPosition) throws RemoteException {

    }

    public void showUpdateChat(Message message) throws RemoteException {

    }

    @Override
    public void showUpdateCurrentPlayer(ClientPlayer currentPlayer, ClientPhase phase) throws RemoteException {

    }

    @Override
    public void showUpdateSuspendedGame() throws RemoteException {

    }

    @Override
    public void showWinners(List<String> winners) throws RemoteException {

    }

    @Override
    public void reportError(String details) {

    }
}
