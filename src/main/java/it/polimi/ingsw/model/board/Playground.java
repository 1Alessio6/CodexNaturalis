package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.card.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Representation of the player's playground.
 * The playground contains a representation of card's disposition and manages their positioning.
 * It also saves and updates player's resources and score.
 */

public class Playground {
    private final Map<Position, Tile> area;
    private int points;
    private final Map<Symbol, Integer> resources;

    /**
     * Constructs a playground according to its condition at the beginning of the game.
     */
    public Playground() {

        this.area = new HashMap<>();
        Position origin = new Position(0, 0);
        Availability s = Availability.EMPTY;
        area.put(origin, new Tile(s));

        this.points = 0;

        this.resources = new HashMap<>();
        resources.put(Symbol.ANIMAL, 0);
        resources.put(Symbol.FUNGI, 0);
        resources.put(Symbol.INKWELL, 0);
        resources.put(Symbol.INSECT, 0);
        resources.put(Symbol.PLANT, 0);
        resources.put(Symbol.MANUSCRIPT, 0);
        resources.put(Symbol.QUILL, 0);
    }



    /**
     * Returns the tile at the position pos.
     * @param pos the position of the tile.
     * @return the tile at position pos or null if there isn't a tile in that position.
     */
    public Tile getTile(Position pos)  {
        return area.get(pos);
    }



    /**
     * Returns true if the tile at position <code>pos</code> has the same availability as <code>availability</code>.
     * If <code>pos</code> is null, or it's not contained in area, the method returns false.
     * @param pos the position of the Tile to check.
     * @param availability to compare to the one in the Tile at position <code>pos</code>.
     * @return true if the tile at position <code>pos</code> has the same availability as <code>availability</code>.
     */
    public boolean sameAvailability(Position pos, Availability availability) {
        if (!area.containsKey(pos))
            return false;

        return area.get(pos).sameAvailability(availability);
    }



    /**
     * Checks if there is a Tile in at specific position.
     * @param position of the Tile to check.
     * @return true if the playground contains a tile in that position.
     */
    public boolean contains(Position position) {
        return area.containsKey(position);
    }



    /**
     * Returns all the position in the playground.
     * @return a set which contains all the position in which there's a tile.
     */
    public Set<Position> getAllPositions() {
        return area.keySet();
    }



    /**
     * Returns all the player's resources.
     * @return a map containing for each resource symbol the specific amount owned by the player.
     */
    public Map<Symbol, Integer> getResources() {
        return new HashMap<>(resources);
    }



    /**
     * Returns the player current score.
     * @return an integer representing player's current score.
     */
    public int getPoints() {
        return points;
    }



    /**
     * Sets the value of the points.
     * @param points the updated value of the points owned by the player.
     */
    public void setPoints(int points) {
        this.points = points;
    }



    /**
     * Returns all the available position.
     * @return a list containing all the position stored in the playground associated to an empty tile.
     */
    public List<Position> getAvailablePositions(){
        return this.area.keySet().stream().filter(x -> this.area.get(x).sameAvailability(Availability.EMPTY)).collect(Collectors.toList());
    }



    /**
     * Places a card's face in the playground.
     * @param c the face that is placed in the playground.
     * @param p the position where the face is placed.
     * @throws UnavailablePositionException if the position it's unavailable or already occupied.
     * @throws NotEnoughResourcesException if the player doesn't have enough resources to place the card's face.
     */
    public void placeCard(Face c, Position p) throws UnavailablePositionException, NotEnoughResourcesException {

        if (!this.area.get(p).sameAvailability(Availability.EMPTY)) {
            throw new UnavailablePositionException("This Position it's not available");
        }

        //requirements check
        if(!checkRequirements(c.getRequiredResources())){
            throw new NotEnoughResourcesException("Insufficient resources");
        }

        int x = p.getX();
        int y = p.getY();

        //update the current tile
        this.area.get(p).setAvailability(Availability.OCCUPIED);
        this.area.get(p).setFace(c);

        CornerPosition corner_pos = null;

        //update for every corner the disposition
        for (CornerPosition current_corner : CornerPosition.values()) {

            Position pos = correspondingPosition(x, y, current_corner);

            if (this.area.get(pos).sameAvailability(Availability.OCCUPIED)) {
                switch (current_corner) { //for each iteration the corner occupied in the card we are covering it is different
                    //corner_pos represents the occupied corner position in the list
                    case TOP_LEFT: //rx low
                        corner_pos = CornerPosition.LOWER_RIGHT;
                        break;
                    case TOP_RIGHT: //sx low
                        corner_pos = CornerPosition.LOWER_LEFT;
                        break;
                    case LOWER_RIGHT: //sx high
                        corner_pos = CornerPosition.TOP_LEFT;
                        break;
                    case LOWER_LEFT: //rx high
                        corner_pos = CornerPosition.TOP_RIGHT;
                        break;
                }

                this.area.get(pos).getFace().getCorners().get(corner_pos).setCovered();
                // the placement may cover a resource
                Symbol s = this.area.get(pos).getFace().getCorners().get(corner_pos).getSymbol();
                this.resources.put(s, this.resources.get(s) - 1);
            }

            if (c.getCorners().containsKey(current_corner)) {
                // check whether the current position define a new available position
                if (!this.area.containsKey(pos)) {
                    this.area.put(pos, new Tile(Availability.EMPTY));
                }
            } else { //this branch it's never followed when the face is a back
                if (this.area.containsKey(pos)) {
                    // the placement may cause another adjacent corner card to become invalid without covering it.
                    if (this.area.get(pos).sameAvailability(Availability.EMPTY)) {
                        this.area.get(pos).setAvailability(Availability.NOTAVAILABLE);
                    }
                } else {
                    // not available position
                    this.area.put(pos, new Tile(Availability.NOTAVAILABLE));
                }
            }
        }

        updateResources(c);
        this.points = this.points + c.calculatePoints(p,this);

    }



    /**
     * Updates player's resources.
     * @param f the face containing the resources to add to player's resources.
     */
    private void updateResources(Face f) {
        Map<Symbol, Integer> x = f.getResources(); //needs to be implemented in face
        for (Symbol s : x.keySet()) {
            this.resources.put(s, this.resources.get(s) + x.get(s));
        }
    }



    /**
     * Checks if the playground satisfies face's requirements.
     * @param req the resources required by the face in order to be placed.
     * @return true if the playground contains enough resource to place te card's face.
     */
    private boolean checkRequirements(Map<Symbol, Integer> req){
        for (Symbol s : req.keySet()) {
            if (this.resources.get(s) < req.get(s)) {
                return false;
            }
        }
        return true;
    }



    /**
     * Returns the position on the playground associated to the position of a corner on a card's face.
     * @param x the abscissa of face's position.
     * @param y the ordinate of face's position.
     * @param corner the corner position on the card's face.
     * @return the position in the playground associated to the corner position on the face.
     */
    private Position correspondingPosition(int x, int y, CornerPosition corner){

        int k,j;

        if (corner == CornerPosition.TOP_LEFT || corner == CornerPosition.TOP_RIGHT) {
            j = y + 1;
        } else {
            j = y - 1;
        }

        if (corner == CornerPosition.TOP_RIGHT || corner == CornerPosition.LOWER_RIGHT) {
            k = x + 1;
        } else {
            k = x - 1;
        }

        return new Position(k, j);
    }



    /**
     * Exception thrown when the resource aren't enough.
     * This exception indicates the player resources are less than resources needed to place the card's
     * face he's trying to place.
     */
    public static class NotEnoughResourcesException extends Exception {
        public NotEnoughResourcesException(String message) {
            super(message);
        }
    }



    /**
     * Exception thrown when the position is unavailable.
     * This exception indicates the position where the player is placing the card isn't contained in
     * his playground, or it's not empty the tile.
     */
    public static class UnavailablePositionException extends Exception {
        public UnavailablePositionException(String message) {
            super(message);
        }
    }

}
