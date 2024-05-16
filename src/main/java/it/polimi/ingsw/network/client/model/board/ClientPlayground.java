package it.polimi.ingsw.network.client.model.board;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Face;
import it.polimi.ingsw.model.card.Symbol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientPlayground implements Serializable {
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

    public ClientPlayground(Playground playgroundToCopy) {
        area = createClientArea(playgroundToCopy.getArea());
        resources = playgroundToCopy.getResources();
        points = playgroundToCopy.getPoints();
    }

    private Map<Position, ClientTile> createClientArea(Map<Position, Tile> areaToCopy){

        Map<Position, ClientTile> area = new HashMap<>();

        for(Position position : areaToCopy.keySet()){
            area.put(position, new ClientTile(areaToCopy.get(position)));
        }

        return area;
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

    public void placeTile(Position position, ClientTile tile) {
        this.area.put(position, tile);
    }

    public List<Position> getAvailablePositions() {
        return this.area.keySet().stream().filter(x -> this.area.get(x).sameAvailability(Availability.EMPTY)).collect(Collectors.toList());
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

    public void updateResources(Symbol symbol, int amount) {
        this.resources.put(symbol, amount);
    }// updateResources doesn't calculate the sum of the different calculated points, it only updates the actual amount of a particular
    // symbol in the resources map

    public void addNewAvailablePositions(List <Position> newAvailablePosition){
        for(Position position : newAvailablePosition){
            placeTile(position, new ClientTile(Availability.EMPTY));
        }
    }

    public ClientTile getTile(Position position){
        if(area.containsKey(position)){
            return area.get(position);
        }
        return null;
    }

    public void setCoveredCorner(Map<Position, CornerPosition> coveredCorner){
        for(Position position : coveredCorner.keySet()){
            getTile(position).getFace().setCornerCovered(coveredCorner.get(position));
        }
    }

    public void updateResources(Map<Symbol, Integer> newResources) {
        for (Symbol symbol : newResources.keySet()) {
            updateResources(symbol, newResources.get(symbol));
        }
    }

}
