package it.polimi.ingsw.network.client.model.board;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.card.CornerPosition;

import java.util.Map;

public class ClientTile {
    private int faceID;
    //private boolean isUsed; a variable used to check if the card has been already used for redeeming the points of an objective card
    private Availability availability;

    private Map<CornerPosition, Boolean> coveredCorner; //boolean True if the corner at the given position it's covered, if there isn't a position in the map the corner it's considered not existing


    /**
     * Constructs a tile which doesn't contain a face with the parameter's availability.
     * @param a the tile's availability.
     */
    public ClientTile(Availability a){ //this constructor method it's used when we create an empty Tile that could be available or not available
        faceID = -1; //empty tile, equivalent to null into Playground
        availability = a;
    }



    /**
     * Returns the face contained in the tile.
     * @return a face representing the card's face placed in the tile.
     */
    public int getFace() {
        return faceID;
    }

    public void setCoveredCorner(Map<CornerPosition, Boolean> coveredCorner) {
        assert(this.faceID != -1);
        this.coveredCorner = coveredCorner;
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
     * @param faceID the ID of the new face contained in the tile.
     */
    public void setFace(int faceID) {
        this.faceID = faceID;
    }

    public String toString(){
        String face = "EmptyTile";
        if(this.getFace() != -1){
            face = String.valueOf(this.getFace());
        }
        return availability + "  " + face;
    }
}
