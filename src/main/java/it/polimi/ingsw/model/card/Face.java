package it.polimi.ingsw.model.card;

import java.util.List;

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
}
