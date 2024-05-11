package it.polimi.ingsw.network.client.model;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.model.board.ClientBoard;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientGame {
    private List<ClientCard> faceUpCards;
    private List<ClientCard> commonObjects;
    private int currentPlayerIdx; // index in the current player list.
    private List<ClientPlayer> players;
    private List<Message> messages;
    private ClientBoard clientBoard;
    private ClientPhase currentPhase;

    private boolean isGameActive;

    public boolean isGameActive() {
        return isGameActive;
    }

    public void setGameActive(boolean gameActive) {
        isGameActive = gameActive;
    }

    public ClientPhase getCurrentPhase() {
        return currentPhase;
    }

    public List<ClientPlayer> getPlayers() {
        return players;
    }

    public ClientPlayer getPlayer(String username) {
        for(ClientPlayer player : players){
            if(player.getUsername().equals(username)){
                return player;
            }
        }
        return null;
    }


    public void setCurrentPhase(ClientPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public List<ClientCard> getFaceUpCards() {
        return faceUpCards;
    }

    public List<ClientCard> getCommonObjects() {
        return commonObjects;
    }

    public int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public ClientPlayer getPlayer(int numPlayer) {
        return players.get(numPlayer);
    }

    public ClientPlayer getMainPlayer(){
        return getPlayer(0);
    }

    public void setCurrentMainPlayer(boolean isCurrent) {
        getMainPlayer().setIsCurrentPlayer(isCurrent);
    }

    public ClientCard getMainPlayerCard(int cardHandPosition){
        return getMainPlayer().getPlayerCard(cardHandPosition);
    }

    public String getMainPlayerUsername(){
        return getMainPlayer().getUsername();
    }

    public ClientPlayground getMainPlayerPlayground(){
        return getMainPlayer().getPlayground();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public ClientBoard getClientBoard() {
        return clientBoard;
    }

    public ClientGame(Game game) {
        faceUpCards = new ArrayList<>();
        for (Card faceUpCard : game.getFaceUpCards()) {
            faceUpCards.add(new ClientCard(faceUpCard));
        }
        commonObjects = new ArrayList<>();
        for (ObjectiveCard commonObjective : game.getCommonObjectives()) {
            commonObjects.add(new ClientCard(commonObjective));
        }
        currentPlayerIdx = game.getCurrentPlayerIdx();
        players = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            ClientPlayer clientPlayer = new ClientPlayer(player);
            if (player.getUsername().equals(game.getCurrentPlayer().getUsername())) {
                clientPlayer.setIsCurrentPlayer(true);
            }
            players.add(clientPlayer);
        }

        messages = game.getMessages();

        // todo. complete method
        //clientBoard = new ClientBoard();

      //  private ClientBoard clientBoard;
      //  private ClientPhase currentPhase;
    }

    public boolean isGoldenDeckEmpty(){
        return clientBoard.isGoldenDeckEmpty();
    }

    public boolean isResourceDeckEmpty(){
        return clientBoard.isResourceDeckEmpty();
    }

    public ClientCard getFaceUpCard(int index){
        return clientBoard.getFaceUpCards()[index];
    }

    public boolean isFaceUpSlotEmpty(int index){
        return getFaceUpCard(index) == null;
    }

    public Set<PlayerColor> getAlreadyTakenColors(){
        Set<PlayerColor> alreadyTakenColors = new HashSet<>();

        for(ClientPlayer player : players){
            if(player.getColor() != null){
                alreadyTakenColors.add(player.getColor());
            }
        }

        return alreadyTakenColors;
    }

    public boolean containsPlayer(String username){
        return players.contains(getPlayer(username));
    }

    public void addPlayer(String username){
        players.add(new ClientPlayer(username));
    }

    public ClientPlayground getPlaygroundByUsername(String username){
        return getPlayer(username).getPlayground();
    }
}


