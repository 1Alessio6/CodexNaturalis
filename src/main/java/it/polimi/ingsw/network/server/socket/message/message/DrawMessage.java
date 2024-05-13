<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/DrawMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/DrawMessage.java

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
