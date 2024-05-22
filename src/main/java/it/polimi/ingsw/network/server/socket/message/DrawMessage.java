package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class DrawMessage extends NetworkMessage {
    private int idToDraw;

    public DrawMessage(String sender, int idToDraw) {
        super(Type.DRAW, sender);
        this.idToDraw = idToDraw;
    }

    public int getIdDraw() {
        return idToDraw;
    }
}
