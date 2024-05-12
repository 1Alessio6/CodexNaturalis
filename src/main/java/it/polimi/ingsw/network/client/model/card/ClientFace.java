package it.polimi.ingsw.network.client.model.card;

import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Face;

import java.util.Map;

public class ClientFace {
    private final int faceID;
    private Map<CornerPosition, Corner> corners; //todo could be changed with a client corner which doesn't have a symbol

    public ClientFace() {
        faceID = -1;
    }

    public ClientFace(Face face){
        faceID = face.getId();
        corners = face.getCorners();
    }

    //todo improve
    public ClientFace(int faceID) {
        this.faceID = faceID;
        this.corners = null;
    }

    public int getFaceID() {
        return faceID;
    }

    public Map<CornerPosition, Corner> getCorners() {
        return corners;
    }

    public void setCornerCovered(CornerPosition cornerPosition) {
        assert(corners.containsKey(cornerPosition));
        corners.get(cornerPosition).setCovered();
    }
}
