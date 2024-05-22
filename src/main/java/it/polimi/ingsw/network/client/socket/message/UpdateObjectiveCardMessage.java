package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;

public class UpdateObjectiveCardMessage extends NetworkMessage {

    private final ClientObjectiveCard chosenObjective;

    private final String username;

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
