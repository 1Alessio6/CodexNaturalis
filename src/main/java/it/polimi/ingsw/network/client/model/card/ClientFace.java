package it.polimi.ingsw.network.client.model.card;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ClientFace implements Serializable {
    private final int faceID;

    private Map<CornerPosition, Corner> corners; //todo could be changed with a client corner which doesn't have a symbol

    private CardColor color;

    private Map<Symbol, Integer> backCenterResources;

    private int score;

    private Condition pointsCondition;

    private Map<Symbol, Integer> requirements;

    public ClientFace() {
        faceID = -1;
    }

    public ClientFace(Face face) {
        faceID = face.getId();
        corners = face.getCorners();
        score = face.getScore();
        color = face.getColor();
        backCenterResources = face.getBackCenterResources();
        pointsCondition = face.getCondition();
        requirements = face.getRequiredResources();
    }

    public ClientFace(int faceID) {
        this.faceID = faceID;
        this.corners = new HashMap<>();
    }

    public int getFaceID() {
        return faceID;
    }

    public Map<CornerPosition, Corner> getCorners() {
        return corners;
    }

    public CardColor getColor() {
        return color;
    }

    public void setCornerCovered(CornerPosition cornerPosition) {
        assert (corners.containsKey(cornerPosition));
        corners.get(cornerPosition).setCovered();
    }

    public Map<Symbol, Integer> getBackCenterResources() {
        return backCenterResources;
    }

    public Map<Symbol, Integer> getRequirements(){
        return requirements;
    }

    public Condition getPointsCondition(){
        return pointsCondition;
    }

    public int getScore() {
        return score;
    }
}
