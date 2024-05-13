<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/PlaceCardMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/PlaceCardMessage.java

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Side;

public class PlaceCardMessage extends ServerMessage {
    private int frontId;
    private int backId;
    private Side side;
    private Position position;

    public PlaceCardMessage(String sender, int frontId, int backId, Side side, Position position) {
        super(sender, ServerType.PLACE_CARD);
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
