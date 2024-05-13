package it.polimi.ingsw.network.server.socket.server.message;

public class PlaceObjectiveMessage extends ServerMessage {
    private int chosenObjective;

    public PlaceObjectiveMessage(String sender, int chosenObjective) {
        super(sender, ServerType.PLACE_OBJECTIVE);
    }

    public int getChosenObjective() {
        return chosenObjective;
    }
}
