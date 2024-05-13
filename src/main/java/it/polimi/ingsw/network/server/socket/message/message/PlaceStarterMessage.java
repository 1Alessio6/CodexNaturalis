<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/PlaceStarterMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/PlaceStarterMessage.java

import it.polimi.ingsw.model.card.Side;

public class PlaceStarterMessage extends ServerMessage {
    private Side side;
    public PlaceStarterMessage(String sender, Side side) {
        super(sender, ServerType.PLACE_STARTER);
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
