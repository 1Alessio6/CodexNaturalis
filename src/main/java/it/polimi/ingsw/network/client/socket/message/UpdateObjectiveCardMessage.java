package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;

/**
 * UpdateObjectiveCardMessage represents the message containing the chosen objective card with which to update.
 */
public class UpdateObjectiveCardMessage extends NetworkMessage {

    private final ClientObjectiveCard chosenObjective;

    private final String username;

    /**
     * Constructs an <code>UpdateObjectiveCardMessage</code> with the <code>chosenObjective</code> and
     * <code>username</code> provided.
     *
     * @param chosenObjective the objective card chosen by the <code>username</code>.
     * @param username        the username of the player who chose the <code>chosenObjective</code>.
     */
    public UpdateObjectiveCardMessage(ClientObjectiveCard chosenObjective, String username) {
        super(Type.SHOW_UPDATE_OBJECTIVE_CARD, "server");
        this.chosenObjective = chosenObjective;
        this.username = username;
    }

    public ClientObjectiveCard getChosenObjective() {
        return chosenObjective;
    }

    public String getUsername() {
        return username;
    }
}
