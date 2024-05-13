package it.polimi.ingsw.network.client.socket.message;

import java.util.List;

public class ShowWinnersMessage extends ClientMessage {

    private final List<String> winners;
    public ShowWinnersMessage(List<String> winners) {
        super(ClientType.SHOW_WINNERS);
        this.winners = winners;
    }

    public List<String> getWinners() {
        return winners;
    }
}
