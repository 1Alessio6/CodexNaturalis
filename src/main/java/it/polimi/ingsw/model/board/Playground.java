package it.polimi.ingsw.model.board;

import java.util.*;

public class Playground {
    private final Map<Position, Tile> area;

    //constructor

    //we could change Map with an HashMap and create the HashMap inside the constructor in order to have a Map with the position (0,0) and with the assurance of having an empty map
    public Playground(Map<Position, Tile> area){
        //area.clear(); area needs to be empty and needs to have stored only the key (0,0)
        Position origin = new Position(0,0);
        Availability s = Availability.EMPTY;
        area.put(origin, new Tile(s));
        this.area = area;
    }

    //getter methods
    public Map<Position, Tile> getArea() {
        return area;
    }
}
