package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.ClientPhase;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientController {

    private VirtualServer server;
    ClientGame game;

    public void updateCreator() throws RemoteException {

    }

    public void updateAfterLobbyCrash() {

    }

    public void updateAfterConnection(ClientGame clientGame) {

    }

    public void updatePlayerStatus(boolean isConnected, String username) {

    }

    public void updateInitialPlayerStatus(ClientPlayer player){

    }

    public void updateBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards){

    }

    public void updateStarterPlacement(String username, int faceId){

    }

    public void updateColor(PlayerColor color, String username){

    }

    public void updateObjectiveCard(ClientCard chosenObjective, String username){

    }

    void updateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard , Position position){

    }

    void updateAfterDraw(ClientCard drawnCard, boolean isEmpty, ClientCard newTopDeck, ClientCard newFaceUpCard, ClientCard newTopCard, boolean additionalTurn, String username, int boardPosition) throws RemoteException {

    }

    //method to notify update after a draw

    void updateChat(Message message){

    }

    void updateCurrentPlayer(ClientPlayer currentPlayer, ClientPhase phase){

    }

    void updateSuspendedGame(){

    }

    void showWinners(List<String> winners){
    }

}
