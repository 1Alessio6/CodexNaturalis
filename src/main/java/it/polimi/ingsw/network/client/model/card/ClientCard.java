package it.polimi.ingsw.network.client.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Side;

import java.io.Serializable;

public class ClientCard implements Serializable {
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

    // copy constructor
    public ClientCard(Card card) {
        this.front = new ClientFace(card.getFace(Side.FRONT));
        this.back = new ClientFace(card.getFace(Side.BACK));
    }

    public int getBackId() {
        return back.getFaceID();
    }

    public int getFrontId() {
        return front.getFaceID();
    }

    public ClientFace getBack() {
        return back;
    }

    public ClientFace getFront() {
        return front;
    }

    public ClientFace getFace(Side side) {
        if (side == Side.BACK) {
            return back;
        }
        return front;
    }

    public String getFrontPath(){
        return front.getPath() + ".png";
    }

    public String getBackPath(){
        return back.getPath() + ".png";
    }

    public String getSidePath(Side side){
        if(side == Side.BACK){
            return getBackPath();
        }
        return getFrontPath();
    }

}
