package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

import java.util.List;

/**
 * ShowWinnersMessage represents the message containing the winners of the game.
 */
public class ShowWinnersMessage extends NetworkMessage {

    private final List<String> winners;

    /**
     * Constructs <code>ShowWinnerMessage</code> with the <code>winners</code> provided.
     *
     * @param winners the winners of the game.
     */
    public ShowWinnersMessage(List<String> winners) {
        super(Type.SHOW_WINNERS, "server");
        this.winners = winners;
    }

    public List<String> getWinners() {
        return winners;
    }
}
