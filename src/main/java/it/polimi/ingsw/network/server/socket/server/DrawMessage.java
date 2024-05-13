package it.polimi.ingsw.network.server.socket.server;

public class DrawMessage extends ServerMessage {
    private int idToDraw;

    public DrawMessage(String sender, int idToDraw) {
        super(sender, ServerType.DRAW);
    }

    public int GetIdDraw() {
        return idToDraw;
    }
}
