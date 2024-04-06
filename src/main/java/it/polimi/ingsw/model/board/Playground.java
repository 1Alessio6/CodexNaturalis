package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.card.*;

import java.util.*;
import java.util.stream.Collectors;

public class Playground {
    private final Map<Position, Tile> area;
    private int points;
    private final Map<Symbol, Integer> resources;

    //constructor

    //we could change Map with an HashMap and create the HashMap inside the constructor in order to have a Map with the position (0,0) and with the assurance of having an empty map
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

    public Tile getTile(Position pos)  {
        return area.get(pos);
    }

    /**
     * Returns true if the tile at position <code>pos</code> has the same availability as <code>availability</code>.
     * If <code>pos</code> is null, or it's not contained in area, the method returns false.
     * @param pos of the Tile to check.
     * @param availability to compare to the one in the Tile at position <code>pos</code>.
     * @return true if the tile at position <code>pos</code> has the same availability as <code>availability</code>.
     */
    public boolean sameAvailability(Position pos, Availability availability) {
        if (!area.containsKey(pos))
            return false;

        return area.get(pos).sameAvailability(availability);
    }

    public boolean contains(Position position) {
        return area.containsKey(position);
    }

    public Set<Position> getAllPositions() {
        return area.keySet();
    }

    public Map<Symbol, Integer> getResources() {
        return new HashMap<>(resources);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Position> getAvailablePositions() {   //It's not required to return an optional because the map has always at least one available position
        return this.area.keySet().stream().filter(x -> this.area.get(x).getAvailability() == Availability.EMPTY).collect(Collectors.toList());
    }

    //hypothesis all the card have in the arraylist the value in clockwise order starting from the top left corner at position zero
    //check if all the methods are called on existing elements of the map

    public void placeCard(Front c, Position p) throws UnavailablePositionException, NotEnoughResourcesException {

        if (this.area.get(p).getAvailability() == Availability.OCCUPIED || this.area.get(p).getAvailability() == Availability.NOTAVAILABLE) {
            throw new UnavailablePositionException("This Position it's not available");
        }

        //requirements check
        Map<Symbol, Integer> req = c.getRequiredResources();
        for (Symbol s : req.keySet()) {
            if (this.resources.get(s) < req.get(s)) {
                throw new NotEnoughResourcesException("Insufficient resources");
            }
        }

        int x = p.getX();
        int y = p.getY();

        //update the current tile
        this.area.get(p).setAvailability(Availability.OCCUPIED);
        this.area.get(p).setFace(c);

        int j, k;
        CornerPosition corner_pos = null;

        //update for every corner the disposition
        for (CornerPosition current_corner : CornerPosition.values()) {

            if (current_corner == CornerPosition.TOP_LEFT || current_corner == CornerPosition.TOP_RIGHT) { //first 2 iteration check the top corners => we're checking position at height y + 1
                j = y + 1;
            } else {
                j = y - 1;
            }

            if (current_corner == CornerPosition.TOP_RIGHT || current_corner == CornerPosition.LOWER_RIGHT) {
                k = x + 1;
            } else {
                k = x - 1;
            }

            Position pos = new Position(k, j);

            if (this.area.get(pos).getAvailability() == Availability.OCCUPIED) {
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
                Symbol s = this.area.get(pos).getFace().getCorners().get(corner_pos).getSymbol();
                this.resources.put(s, this.resources.get(s) - 1);
            }

            if (c.getCorners().containsKey(current_corner)) { //null if the corner it's not an empty corner??
                if (!this.area.containsKey(pos)) {
                    this.area.put(pos, new Tile(Availability.EMPTY));
                }
            } else {
                if (this.area.containsKey(pos)) {
                    if (this.area.get(pos).getAvailability() == Availability.EMPTY) {
                        this.area.get(pos).setAvailability(Availability.NOTAVAILABLE);
                    }
                } else {
                    this.area.put(pos, new Tile(Availability.NOTAVAILABLE));
                }
            }
        }

        updateResources(c);
        this.points = this.points + c.calculatePoints(p,this);

    }

    //all the back of the cards generate only available positions because they haven't hidden corner
    //hypothesis all the card have in the arraylist the value in clockwise order starting from the top left corner at position zero

    //this is the implementation when it's placed a back in an available tile, add an exception for other cases
    public void placeCard(Back c, Position p) throws UnavailablePositionException {

        if (this.area.get(p).getAvailability() == Availability.OCCUPIED || this.area.get(p).getAvailability() == Availability.NOTAVAILABLE) {
            throw new UnavailablePositionException("This Position it's not available");
        }

        int x = p.getX();
        int y = p.getY();

        this.area.get(p).setAvailability(Availability.OCCUPIED);
        this.area.get(p).setFace(c);

        int j, k;
        CornerPosition corner_pos = null;

        for(CornerPosition current_corner : CornerPosition.values()) {

            if (current_corner == CornerPosition.TOP_LEFT || current_corner == CornerPosition.TOP_RIGHT) { //first 2 iteration check the top corners => we're checking position at height y + 1
                j = y + 1;
            } else {
                j = y - 1;
            }

            if (current_corner == CornerPosition.TOP_RIGHT || current_corner == CornerPosition.LOWER_RIGHT) {
                k = x + 1;
            } else {
                k = x - 1;
            }

            Position pos = new Position(k, j);

            //check of old card corner occupied by the new card
            //update of resources covered by the new card

            if (this.area.get(pos).getAvailability() == Availability.OCCUPIED) {
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
                Symbol s = this.area.get(pos).getFace().getCorners().get(corner_pos).getSymbol();
                this.resources.put(s, this.resources.get(s) - 1);
            }

            //generation of new position in the hashmap

            if (!this.area.containsKey(pos)) {
                this.area.put(pos, new Tile(Availability.EMPTY));
            }
        }

        updateResources(c);
    }

    private void updateResources(Face f) {
        Map<Symbol, Integer> x = f.getResources(); //needs to be implemented in face
        for (Symbol s : x.keySet()) {
            this.resources.put(s, this.resources.get(s) + x.get(s));
        }
    }

    public static class NotEnoughResourcesException extends Exception {
        public NotEnoughResourcesException(String message) {
            super(message);
        }
    }

    public static class UnavailablePositionException extends Exception {

        public UnavailablePositionException(String message) {
            super(message);
        }
    }

}
