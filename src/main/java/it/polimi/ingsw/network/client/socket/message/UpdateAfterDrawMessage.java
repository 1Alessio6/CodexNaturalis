package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;

public class UpdateAfterDrawMessage extends NetworkMessage {

    private final ClientCard drawnCard;
    private final ClientFace newTopDeck;
    private final ClientCard newFaceUpCard;
    private final String username;
    private final int boardPosition;
    public UpdateAfterDrawMessage(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) {
        super(Type.SHOW_UPDATE_AFTER_DRAW, "server");
        this.drawnCard = drawnCard;
        this.newTopDeck = newTopDeck;
        this.newFaceUpCard = newFaceUpCard;
        this.username = username;
        this.boardPosition = boardPosition;
    }

    public ClientCard getDrawnCard() {
        return drawnCard;
    }

    public String getUsername() {
        return username;
    }

    public ClientCard getNewFaceUpCard() {
        return newFaceUpCard;
    }

    public int getBoardPosition() {
        return boardPosition;
    }

    public ClientFace getNewTopDeck() {
        return newTopDeck;
    }
}
