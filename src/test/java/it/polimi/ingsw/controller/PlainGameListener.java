package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameListener;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.color.PlayerColor;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamephase.GamePhase;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;

import java.util.List;
import java.util.Map;

public class PlainGameListener implements GameListener {

    @Override
    public void updateAfterConnection(ClientGame clientGame) {

    }

    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) {

    }

    @Override
    public void showUpdateExceedingPlayer() {

    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username){

    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) {

    }

    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {

    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) {

    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition){

    }

    @Override
    public void showUpdateChat(Message message) {

    }

    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) {

    }

    @Override
    public void showUpdateGameState() {

    }

    @Override
    public void showWinners(List<String> winners) {

    }

    @Override
    public void reportError(String details) {

    }

    @Override
    public void resultOfLogin(boolean accepted, String details) {

    }
}
