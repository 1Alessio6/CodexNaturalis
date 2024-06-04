package it.polimi.ingsw.model.lobby;

import it.polimi.ingsw.model.card.InvalidCardIdException;

/**
 * Invalid Players Number Exception is thrown when the number of players established by the first player is not within
 * the specified range
 */
public class InvalidPlayersNumberException extends Exception {
    public InvalidPlayersNumberException() {
        super("Invalid players number. Set a number between 2 and 4");
    }
}
