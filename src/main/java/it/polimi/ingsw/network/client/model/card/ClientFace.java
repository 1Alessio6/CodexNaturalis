package it.polimi.ingsw.network.client.model.card;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ClientFace implements Serializable {
    private final int faceID;

    private Map<CornerPosition, Corner> corners;

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
        Corner corner = corners.get(cornerPosition);
        assert (corner != null);
        corner.setCovered();
    }

    public String getPath(){
        return "gui/png/cards/" + faceID + ".png";
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
