package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.card.Face;

/**
 * Representation of a tile contained at a specific position of Playground.
 */

public class Tile {
    private Face face;
    //private boolean isUsed; a variable used to check if the card has been already used for redeeming the points of an objective card
    private Availability availability;


    /**
     * Constructs a tile which doesn't contain a face with the parameter's availability.
     * @param a the tile's availability.
     */
    public Tile(Availability a){ //this constructor method it's used when we create an empty Tile that could be available or not available
        face = null;
        availability = a;
    }



    /**
     * Returns the face contained in the tile.
     * @return card's face placed in the tile.
     */
    public Face getFace() {
        return face;
    }



    /**
     * Checks if the availability of the tile it's equal to parameter's availability.
     * @param availability the availability compared.
     */
    public boolean sameAvailability(Availability availability) {
        return this.availability == availability;
    }



    /**
     * Sets the availability.
     * @param availability the new availability of the tile.
     */
    public void setAvailability(Availability availability) {
        this.availability = availability;
    }



    /**
     * Sets the availability.
     * @param face the new face contained in the tile.
     */
    public void setFace(Face face) {
        this.face = face;
    }
}
