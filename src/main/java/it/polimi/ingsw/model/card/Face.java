package it.polimi.ingsw.model.card;

import java.util.*;

public class Face {
    private List<Corner> cornerList;
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Corner> getCornerList() {
        return cornerList;
    }

    public void setCornerList(List<Corner> cornerList) {
        this.cornerList = cornerList;
    }

    public Map<Symbol, Integer> getResources(){
        return new HashMap<Symbol,Integer>();
    }
}
