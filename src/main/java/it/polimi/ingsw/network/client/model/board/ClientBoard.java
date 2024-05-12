package it.polimi.ingsw.network.client.model.board;

import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;


public class ClientBoard {

    private ClientCard[] faceUpCards;

    private ClientFace goldenDeckTopBack; //contains only backID numbers

    private ClientFace resourceDeckTopBack; //contains only backID numbers

    private ClientCard[] commonObjectives;

    public ClientBoard() {
        faceUpCards = new ClientCard[4];
        commonObjectives = new ClientCard[2];
    }

    //getter methods


    public ClientFace getGoldenDeckTopBack() {
        return goldenDeckTopBack;
    }

    public ClientFace getResourceDeckTopBack() {
        return resourceDeckTopBack;
    }

    public ClientCard[] getFaceUpCards() {
        return faceUpCards;
    }

    public ClientCard[] getCommonObjectives() {return commonObjectives;}

    //setter methods

    public void setGoldenDeckTopBack(ClientFace goldenDeckTopBack) {
        this.goldenDeckTopBack = goldenDeckTopBack;
    }

    public void setResourceDeckTopBack(ClientFace resourceDeckTopBack) {
        this.resourceDeckTopBack = resourceDeckTopBack;
    }

    public void setCommonObjectives(ClientCard[] commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    public void addFaceUpCards(ClientCard faceUpCard, int positionFaceUp) {
        this.faceUpCards[positionFaceUp] = faceUpCard;
    }

    public boolean isGoldenDeckEmpty(){
        return goldenDeckTopBack == null;
    }

    public boolean isResourceDeckEmpty(){
        return resourceDeckTopBack == null;
    }

}
