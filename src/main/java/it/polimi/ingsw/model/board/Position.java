package it.polimi.ingsw.model.board;

public class Position {

    //attributes
    private final int x;
    private final int y;

    //constructor
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    //getter methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //nd compare


    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null || this.getClass() != obj.getClass()){
            return false;
        }

        return this.getX() == ((Position) obj).getX() && this.getY() == ((Position) obj).getY();
    }

    @Override
    public int hashCode() { //it doesn't unify a unique hashcode for all couples, but it's not necessary, eventually the conflicts are solved with equals method
        return this.x * this.y;
    }
}
