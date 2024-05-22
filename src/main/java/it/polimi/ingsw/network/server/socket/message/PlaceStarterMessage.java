package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class PlaceStarterMessage extends NetworkMessage {
    private Side side;

    public PlaceStarterMessage(String sender, Side side) {
        super(Type.PLACE_STARTER, sender);
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
