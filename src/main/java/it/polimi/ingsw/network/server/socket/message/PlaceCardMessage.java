package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * PlaceCardMessage represents the message that allows a player to place a card in the playground.
 */
public class PlaceCardMessage extends NetworkMessage {
    private int frontId;
    private int backId;
    private Side side;
    private Position position;

    /**
     * Constructs a <code>PlaceCardMessage</code> with the <code>sender</code>, <code>frontId</code>,
     * <code>backId</code>, <code>side</code> and <code>position</code> provided.
     *
     * @param sender   the username of the player who performs the placement.
     * @param frontId  the identification of the front of the card.
     * @param backId   the identification of the back of the card.
     * @param side     the selected side of the card
     * @param position the selected position to place the card.
     */
    public PlaceCardMessage(String sender, int frontId, int backId, Side side, Position position) {
        super(Type.PLACE_CARD, sender);
        this.frontId = frontId;
        this.backId = backId;
        this.side = side;
        this.position = position;
    }

    public int getFrontId() {
        return frontId;
    }

    public int getBackId() {
        return backId;
    }

    public Side getSide() {
        return side;
    }

    public Position getPosition() {
        return position;
    }
}
