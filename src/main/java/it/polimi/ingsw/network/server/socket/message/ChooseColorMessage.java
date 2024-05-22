package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class ChooseColorMessage extends NetworkMessage {
    private PlayerColor color;

    public ChooseColorMessage(String sender, PlayerColor color) {
        super(Type.CHOOSE_COLOR, sender);
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }
}
