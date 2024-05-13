package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.model.card.Color.PlayerColor;

public class ChooseColorMessage extends ServerMessage {
    private PlayerColor color;

    public ChooseColorMessage(String sender, PlayerColor color) {
        super(sender, ServerType.CHOOSE_COLOR);
    }

    public PlayerColor getColor() {
        return color;
    }
}
