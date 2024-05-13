<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/PlaceObjectiveMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/PlaceObjectiveMessage.java

public class PlaceObjectiveMessage extends ServerMessage {
    private int chosenObjective;

    public PlaceObjectiveMessage(String sender, int chosenObjective) {
        super(sender, ServerType.PLACE_OBJECTIVE);
    }

    public int getChosenObjective() {
        return chosenObjective;
    }
}
