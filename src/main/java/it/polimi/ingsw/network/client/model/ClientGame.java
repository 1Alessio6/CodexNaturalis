package it.polimi.ingsw.network.client.model;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.client.model.board.ClientBoard;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.util.List;

public class ClientGame {
    private List<ClientCard> faceUpCards;
    private List<ClientCard> commonObjects;
    private int currentPlayerIdx; // index in the current player list.
    private List<ClientPlayer> players;
    private List<Message> messages;
    private ClientBoard clientBoard;

    public List<ClientCard> getFaceUpCards() {
        return faceUpCards;
    }

    public List<ClientCard> getCommonObjects() {
        return commonObjects;
    }

    public int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public List<ClientPlayer> getPlayers() {
        return players;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public ClientBoard getClientBoard() {
        return clientBoard;
    }

    public ClientGame(Game game) {
        // todo. add logic to construct from a Game
        // note. client board can be obtained from previous information
    }
}


