package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.card.Symbol;

import java.util.HashMap;
import java.util.Map;

public class ClientPlayground {
    private final Map<Position, ClientTile> area;
    private int points;
    private final Map<Symbol, Integer> resources;

    public ClientPlayground(Map<Position, ClientTile> area, Map<Symbol, Integer> resources) {
        this.area = new HashMap<>();
        Position origin = new Position(0, 0);
        Availability s = Availability.EMPTY;
        area.put(origin, new ClientTile(s));

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

    public Map<Position, ClientTile> getArea() {
        return area;
    }

    public Map<Symbol, Integer> getResources() {
        return resources;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void placeTile(Position position, ClientTile tile){
        this.area.put(position,tile);
    }

    public String toString() {

        return "Area: \n" + areaToString() + "\n\nResources:\n" + this.resources.toString() + "\n\nScore:\n" + points;

    }

    private String areaToString() {
        StringBuilder areaString = new StringBuilder();

        for (Position p : this.area.keySet()) {
            areaString.append("( ").append(p).append(" ) --> ").append(this.area.get(p)).append("\n");
        }

        return String.valueOf(areaString);
    }
}
