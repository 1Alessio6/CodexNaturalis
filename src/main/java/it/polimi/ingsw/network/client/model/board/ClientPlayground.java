package it.polimi.ingsw.network.client.model.board;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Symbol;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Representation of the player's playground.
 */
public class ClientPlayground implements Serializable {
    private final Map<Position, ClientTile> area;
    private int points;
    private final Map<Symbol, Integer> resources;

    List<Position> positioningOrder;

    /**
     * Constructs a playground in accordance to its initial condition.
     *
     * @param area      of the game.
     * @param resources in the game.
     */
    public ClientPlayground(Map<Position, ClientTile> area, Map<Symbol, Integer> resources) {
        this.area = new HashMap<>();
        Position origin = new Position(0, 0);
        Availability s = Availability.EMPTY;
        area.put(origin, new ClientTile(s));
        positioningOrder = new ArrayList<>();
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
     * Constructs a playground given a <code>playgroundToCopy</code>.
     *
     * @param playgroundToCopy playground to be copied.
     */
    public ClientPlayground(Playground playgroundToCopy) {
        area = createClientArea(playgroundToCopy.getArea());
        positioningOrder = new ArrayList<>(playgroundToCopy.getPositioningOrder());
        resources = new HashMap<>(playgroundToCopy.getResources());
        points = playgroundToCopy.getPoints();
    }

    /**
     * Creates an area given an <code>areaToCopy</code>.
     *
     * @param areaToCopy area to be copied.
     * @return the copied area.
     */
    private Map<Position, ClientTile> createClientArea(Map<Position, Tile> areaToCopy){

        Map<Position, ClientTile> area = new HashMap<>();

        for(Position position : areaToCopy.keySet()){
            if(areaToCopy.get(position).getAvailability() != Availability.NOTAVAILABLE){
                area.put(position, new ClientTile(areaToCopy.get(position)));
            }

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

    /**
     * Places a <code>tile</code> in the playground.
     *
     * @param position the position where the tile is placed.
     * @param tile     the tile that is placed in the playground.
     */
    public void placeTile(Position position, ClientTile tile) {

        this.area.put(position, tile);
        if(tile.sameAvailability(Availability.OCCUPIED)){
            this.positioningOrder.add(position);
        }
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

    /**
     * Updates the current amount of a particular <code>symbol</code> in the resources map
     *
     * @param symbol the symbol of the resource to be updated.
     * @param amount the new amount of symbols in the playground.
     */
    public void updateResources(Symbol symbol, int amount) {
        this.resources.put(symbol, amount);
    }// updateResources doesn't calculate the sum of the different calculated points, it only updates the actual amount of a particular
    // symbol in the resources map

    /**
     * Adds <code>newAvailablePosition</code> to the playground
     *
     * @param newAvailablePosition list of new available positions to be added.
     */
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

    /**
     * Updates the amount of <code>newResources</code>.
     *
     * @param newResources to be updated.
     */
    public void updateResources(Map<Symbol, Integer> newResources) {
        for (Symbol symbol : newResources.keySet()) {
            updateResources(symbol, newResources.get(symbol));
        }
    }

    /**
     * Returns all the position in the playground.
     *
     * @return a set which contains all the position available and occupied.
     */
    public Set<Position> getAllPositions() {
        return area.keySet();
    }

    /**
     * Returns the maximum x and the maximum y in absolute value of the tiles' position in the playground.
     * @return an array where the first index is the maximum x and the second one is the maximum y.
     */
    public int[] getRange() {
        int xMax = 0;
        int yMax = 0;
        List<Position> allPositionsInPlayground = new ArrayList<>(area.keySet());

        for (Position pos : allPositionsInPlayground) {
            int xPosAbs = Math.abs(pos.getX());
            int yPosAbs = Math.abs(pos.getY());

            if (xMax < xPosAbs) {
                xMax = xPosAbs;
            }

            if (yMax < yPosAbs) {
                yMax = yPosAbs;
            }
        }

        return new int[]{xMax, yMax};
    }

    public int[] getXMaxAndMin() {
        int xMax = 0;
        int xMin = 0;
        for (Integer currX : area.keySet().stream().map(Position::getX).collect(Collectors.toSet())) {
            xMax = Math.max(currX, xMax);
            xMin = Math.min(currX, xMin);
        }

        return new int[]{xMax, xMin};
    }

    public int[] getYMaxAndMin() {
        int yMax = 0;
        int yMin = 0;
        for (Integer currX : area.keySet().stream().map(Position::getX).collect(Collectors.toSet())) {
            yMax = Math.max(currX, yMax);
            yMin = Math.min(currX, yMin);
        }

        return new int[]{yMax, yMin};
    }

    public List<Position> getPositioningOrder() {
        return positioningOrder;
    }
}

