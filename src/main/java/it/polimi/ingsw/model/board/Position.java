package it.polimi.ingsw.model.board;

public class Position {

    //attributes
    private final int x;
    private final int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    //methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
