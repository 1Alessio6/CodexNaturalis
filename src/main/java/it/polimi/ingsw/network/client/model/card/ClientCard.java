package it.polimi.ingsw.network.client.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Side;

import java.io.Serializable;

/**
 * Represents a card in the client's side.
 */
public class ClientCard implements Serializable {
    ClientFace front;
    ClientFace back;

    /**
     * Constructs a Client Cards with no parameters provided.
     */
    public ClientCard() {
        front = new ClientFace();
        back = new ClientFace();
    }

    /**
     * Constructs a Client Card with the <code>frontID</code> and <code>backID</code> provided.
     *
     * @param frontID of the card.
     * @param backID  of the card.
     */
    public ClientCard(int frontID, int backID) {
        front = new ClientFace(frontID);
        back = new ClientFace(backID);
    }

    /**
     * Constructor used to copy the provided <code>card</code>.
     *
     * @param card from which the tile is to be constructed.
     */
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
        return front.getPath();
    }

    public String getBackPath(){
        return back.getPath();
    }

    public String getSidePath(Side side){
        if(side == Side.BACK){
            return getBackPath();
        }
        return getFrontPath();
    }

}
