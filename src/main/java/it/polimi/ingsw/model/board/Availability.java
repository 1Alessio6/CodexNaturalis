package it.polimi.ingsw.model.board;

/**
 * Enumeration representing the availability of a Tile stored in Playground.
 */

public enum Availability {


    /**
     * Empty, when the tile it's available and a card's face can be placed in there.
     */
    EMPTY,


    /**
     * Not Available when the tile can't contain any face because of an eventual conflict with the
     * corner of another card's face contained in a near tile.
     */
    NOTAVAILABLE,


    /**
     * Empty, when the tile already contains a card's face.
     */
    OCCUPIED
}
