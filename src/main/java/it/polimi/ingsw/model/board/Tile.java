package it.polimi.ingsw.model.board;

public class Tile {
    private Face face;
    //private boolean isUsed; a variable used to check if the card has been already used for redeeming the points of an objective card
    private Availability availability;

    //constructor

    public Tile(Availability a){ //this constructor method it's used when we create an empty Tile
        face = null;
        availability = a;
    }

    public Tile(Face f, Availability a){
        face = f;
        availability = a;
    }

    //getter methods
    public Face getFace() {
        return face;
    }
    public Availability getAvailability() {
        return availability;
    }

    //setter methods
    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public void setFace(Face face) {
        this.face = face;
    }
}
