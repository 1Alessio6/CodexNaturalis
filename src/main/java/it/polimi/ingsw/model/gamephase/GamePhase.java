package it.polimi.ingsw.model.gamephase;

import java.io.Serializable;

/**
 * Representation of a possible game's state.
 * It defines which operations are allowed based on the overall information.
 */

public enum GamePhase implements Serializable {

    /**
     * Set up refers to the phase in which the username, color, starter and objective card have been selected
     */
    Setup,

    /**
     * Place Normal refers to the phase in which the player is allowed to place a card in a normal situation
     */
    PlaceNormal,

    /**
     * Draw Normal refers to the phase in which the player is allowed to draw a card in a normal situation
     */
    DrawNormal,

    /**
     * Place Additional refers to the phase during which the player is allowed to place a card once 20 points have been achieved
     */
    PlaceAdditional,

    /**
     * End refers to the ending phase
     */
    End
}
