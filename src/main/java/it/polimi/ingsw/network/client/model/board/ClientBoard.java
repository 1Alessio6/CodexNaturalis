package it.polimi.ingsw.network.client.model.board;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Face;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ClientBoard implements Serializable {

    private List<ClientCard> faceUpCards;

    private ClientFace goldenDeckTopBack; //contains only backID numbers

    private ClientFace resourceDeckTopBack; //contains only backID numbers

    private List<ClientObjectiveCard> commonObjectives;

    public ClientBoard(List<Card> faceUpCards, List<ObjectiveCard> commonObjectives, Face goldenDeckTopBack, Face resourceDeckTopBack) {
        this.goldenDeckTopBack = goldenDeckTopBack == null ? null : new ClientFace(goldenDeckTopBack);
        this.resourceDeckTopBack = resourceDeckTopBack == null ? null : new ClientFace(resourceDeckTopBack);
        this.faceUpCards = new ArrayList<>();
        for(Card faceUpCard : faceUpCards){
            this.faceUpCards.add(faceUpCard == null ? null : new ClientCard(faceUpCard));
        }
        this.commonObjectives = new ArrayList<>();
        for(ObjectiveCard card : commonObjectives){
            this.commonObjectives.add(new ClientObjectiveCard(card));
        }
    }

    //getter methods


    public ClientFace getGoldenDeckTopBack() {
        return goldenDeckTopBack;
    }

    public ClientFace getResourceDeckTopBack() {
        return resourceDeckTopBack;
    }

    public List<ClientCard> getFaceUpCards() {
        return faceUpCards;
    }

    public List<ClientObjectiveCard> getCommonObjectives() {
        return commonObjectives;
    }

    //setter methods

    public void setGoldenDeckTopBack(ClientFace goldenDeckTopBack) {
        this.goldenDeckTopBack = goldenDeckTopBack;
    }

    public void setResourceDeckTopBack(ClientFace resourceDeckTopBack) {
        this.resourceDeckTopBack = resourceDeckTopBack;
    }

    public void addFaceUpCards(ClientCard faceUpCard, int positionFaceUp) {
        this.faceUpCards.add(positionFaceUp, faceUpCard);
    }

    public boolean isGoldenDeckEmpty() {
        return goldenDeckTopBack == null;
    }

    public boolean isResourceDeckEmpty() {
        return resourceDeckTopBack == null;
    }

}
