package it.polimi.ingsw.network.client.model.card;

import it.polimi.ingsw.model.card.Symbol;

import java.util.HashMap;
import java.util.Map;

public class ClientCard {
    ClientFace front;
    ClientFace back;

    public ClientCard() {
        front = new ClientFace();
        back = new ClientFace();
    }

    public ClientCard(int frontID, int backID) {
        front = new ClientFace(frontID);
        back = new ClientFace(backID);
    }

    public int getBackId() {
        return back.getFaceID();
    }

    public int getFrontId() {
        return front.getFaceID();
    }

    public Map<Symbol, Integer> getRequiredResources() {
        return new HashMap<>();
    }

}
