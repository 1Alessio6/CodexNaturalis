package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;

public class UpdateObjectiveCardMessage extends ClientMessage {

    private final ClientObjectiveCard chosenObjective;

    private final String username;

    public UpdateObjectiveCardMessage(ClientObjectiveCard chosenObjective, String username) {
        super(ClientType.SHOW_UPDATE_OBJECTIVE_CARD);
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
