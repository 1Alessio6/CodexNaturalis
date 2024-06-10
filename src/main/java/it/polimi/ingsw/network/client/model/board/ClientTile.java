package it.polimi.ingsw.network.client.model.board;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Face;
import it.polimi.ingsw.network.client.model.card.ClientFace;

import java.io.Serializable;
import java.util.Map;

/**
 * Representation of a tile contained at a specific position of Client's Playground.
 */
public class ClientTile implements Serializable {
    private ClientFace face;
    //private boolean isUsed; a variable used to check if the card has been already used for redeeming the points of an objective card
    private Availability availability;

    /**
     * Constructs a tile given a tile.
     *
     * @param tile from which the tile is to be constructed.
     */
    public ClientTile(Tile tile){
        availability = tile.getAvailability();
        // null if it refers to a tile which has not already a face on it, because it represents an available position
        face = tile.getFace() == null ? null : new ClientFace(tile.getFace());
    }

    /**
     * Constructs a tile which doesn't contain a face with the parameter's availability.
     * @param a the tile's availability.
     */
    public ClientTile(Availability a){ //this constructor method it's used when we create an empty Tile that could be available or not available
        face = new ClientFace(); //empty tile, equivalent to null into Playground
        availability = a;
    }

    /**
     * Constructs a tile given a <code>face</code>.
     *
     * @param face from which the tile is to be constructed.
     */
    public ClientTile(ClientFace face){ //this constructor method it's used when we create an empty Tile that could be available or not available
        this.face = face; //empty tile, equivalent to null into Playground
        availability = Availability.OCCUPIED;
    }

    /**
     * Returns the face contained in the tile.
     * @return a face representing the card's face placed in the tile.
     */
    public ClientFace getFace() {
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
     * @param faceID the ID of the new face contained in the tile.
     */
    public void setFace(int faceID) {
        face = new ClientFace(faceID);
    }

    public String toString(){
        String face = "EmptyTile";
        if(this.getFace().getFaceID() != -1){
            face = String.valueOf(this.getFace());
        }
        return availability + "  " + face;
    }
}
