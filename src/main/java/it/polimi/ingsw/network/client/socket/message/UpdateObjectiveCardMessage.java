package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.client.model.card.ClientCard;

public class UpdateObjectiveCardMessage extends ClientMessage {

    private final ClientCard chosenObjective;

    public UpdateObjectiveCardMessage(ClientCard chosenObjective) {
        super(ClientType.SHOW_UPDATE_OBJECTIVE_CARD);
        this.chosenObjective = chosenObjective;
    }

    public ClientCard getChosenObjective() {
        return chosenObjective;
    }
}
