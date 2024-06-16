package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;

/**
 * UpdateAfterDrawMessage represents the message containing the new information after a draw.
 */
public class UpdateAfterDrawMessage extends NetworkMessage {

    private final ClientCard drawnCard;
    private final ClientFace newTopDeck;
    private final ClientCard newFaceUpCard;
    private final String username;
    private final int boardPosition;

    /**
     * Constructs an <code>UpdateAfterDrawMessage</code> with the <code>drawnCard</code>, <code>newTopDeck</code>,
     * <code>newFaceUpCard</code>, <code>username</code> and <code>boardPosition</code> provided.
     *
     * @param drawnCard     the drawn card.
     * @param newTopDeck    the new card that replaces the previous card that was present in the deck.
     * @param newFaceUpCard the new face up card that replaces the previous one.
     * @param username      the username of the player who performs the drawing.
     * @param boardPosition the position from which the card was selected, 4 for golden deck, 5 for resource deck and
     *                      0, 1, 2 or 3 for face up cards.
     */
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
