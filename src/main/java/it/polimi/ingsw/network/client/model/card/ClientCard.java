package it.polimi.ingsw.network.client.model.card;

import it.polimi.ingsw.GeneralCard;
import it.polimi.ingsw.model.card.Symbol;

import java.util.HashMap;
import java.util.Map;

public class ClientCard implements GeneralCard {
    ClientFace front;
    ClientFace back;
    Map<Symbol, Integer> requirements; /* set? */

    public ClientCard() {
        front = new ClientFace();
        back = new ClientFace();
    }

    public ClientCard(int frontID, int backID) {
        front = new ClientFace(frontID);
        back = new ClientFace(backID);
    }

    // copy constructor
    public ClientCard(GeneralCard other) {
        this.front = new ClientFace(other.getFrontId());
        this.back = new ClientFace(other.getBackId());
        this.requirements = new HashMap<>(other.getRequiredResources());
    }

    @Override
    public int getBackId() {
        return back.getFaceID();
    }

    @Override
    public int getFrontId() {
        return front.getFaceID();
    }

    @Override
    public Map<Symbol, Integer> getRequiredResources() {
        return requirements;
    }
}
