package it.polimi.ingsw.model.gamePhase;

import java.io.Serializable;

/**
 * Representation of a possible game's state.
 * It defines which operations are allowed based on the overall information.
 */

public enum GamePhase implements Serializable {
    Setup,
    PlaceNormal,
    DrawNormal,
    PlaceAdditional,
    End
}
