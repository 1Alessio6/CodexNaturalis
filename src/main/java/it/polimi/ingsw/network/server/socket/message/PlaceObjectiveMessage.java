package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class PlaceObjectiveMessage extends NetworkMessage {
    private int chosenObjective;

    public PlaceObjectiveMessage(String sender, int chosenObjective) {
        super(Type.PLACE_OBJECTIVE, sender);
        this.chosenObjective = chosenObjective;
    }

    public int getChosenObjective() {
        return chosenObjective;
    }
}
