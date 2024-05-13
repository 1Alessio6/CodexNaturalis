package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.client.model.card.ClientCard;

public class UpdateObjectiveCardMessage extends ClientMessage {

    private final ClientCard chosenObjective;

    private final String username;

    public UpdateObjectiveCardMessage(ClientCard chosenObjective, String username) {
        super(ClientType.SHOW_UPDATE_OBJECTIVE_CARD);
        this.chosenObjective = chosenObjective;
        this.username = username;
    }

    public ClientCard getChosenObjective() {
        return chosenObjective;
    }

    public String getUsername() {
        return username;
    }
}
