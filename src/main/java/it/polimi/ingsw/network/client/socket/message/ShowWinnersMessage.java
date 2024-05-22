package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

import java.util.List;

public class ShowWinnersMessage extends NetworkMessage {

    private final List<String> winners;
    public ShowWinnersMessage(List<String> winners) {
        super(Type.SHOW_WINNERS, "server");
        this.winners = winners;
    }

    public List<String> getWinners() {
        return winners;
    }
}
