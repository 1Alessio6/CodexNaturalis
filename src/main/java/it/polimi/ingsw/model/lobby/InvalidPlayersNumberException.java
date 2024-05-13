package it.polimi.ingsw.model.lobby;

import it.polimi.ingsw.model.card.InvalidCardIdException;

public class InvalidPlayersNumberException extends Exception {
    public InvalidPlayersNumberException() {
        super("Invalid players number. Set a number between 2 and 4");
    }
}
