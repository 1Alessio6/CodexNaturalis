package it.polimi.ingsw.network.server.socket.server.message;

public class DrawMessage extends ServerMessage {
    private int idToDraw;

    public DrawMessage(String sender, int idToDraw) {
        super(sender, ServerType.DRAW);
        this.idToDraw = idToDraw;
    }

    public int getIdDraw() {
        return idToDraw;
    }
}
