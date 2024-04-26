package it.polimi.ingsw.model.gamePhase;

/**
 * Representation of a possible game's state.
 * It defines which operations are allowed based on the overall information.
 */

public enum GamePhase {
    Setup,
    PlaceNormal,
    DrawNormal,
    PlaceAdditional,
    End
}
