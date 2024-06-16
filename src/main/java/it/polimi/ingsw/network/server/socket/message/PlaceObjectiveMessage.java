package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * PlaceObjectiveMessage represents the message containing the objective card chosen by the player.
 */
public class PlaceObjectiveMessage extends NetworkMessage {
    private int chosenObjective;

    /**
     * Constructs <code>PlaceObjectiveMessage</code> with the <code>sender</code> and <code>chosenObjective</code>
     * provided.
     *
     * @param sender          the username of the player who chose the <code>chosenObjective</code>.
     * @param chosenObjective the objective card chosen by the <code>sender</code>.
     */
    public PlaceObjectiveMessage(String sender, int chosenObjective) {
        super(Type.PLACE_OBJECTIVE, sender);
        this.chosenObjective = chosenObjective;
    }

    public int getChosenObjective() {
        return chosenObjective;
    }
}
