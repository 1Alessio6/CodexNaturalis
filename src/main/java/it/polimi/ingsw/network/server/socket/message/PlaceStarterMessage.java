package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * PlaceStarterMessage represents the message that allows a player to place the starter card in the playground.
 */
public class PlaceStarterMessage extends NetworkMessage {
    private Side side;

    /**
     * Constructs a <code>PlaceStarterMessage</code> with the <code>sender</code> and <code>side</code> provided.
     *
     * @param sender the username of the player who performs the placement.
     * @param side   the selected side of the starter card.
     */
    public PlaceStarterMessage(String sender, Side side) {
        super(Type.PLACE_STARTER, sender);
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
