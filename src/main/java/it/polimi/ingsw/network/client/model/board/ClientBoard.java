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

/**
 * Representation of the player's playing area.
 * Client Board includes the face up cards, the resource and golden decks and the common objectives.
 */
public class ClientBoard implements Serializable {

    private List<ClientCard> faceUpCards;

    private ClientFace goldenDeckTopBack; //contains only backID numbers

    private ClientFace resourceDeckTopBack; //contains only backID numbers

    private List<ClientObjectiveCard> commonObjectives;

    /**
     * Constructs a client with the <code>faceUpCards</code>, <code>commonObjectives</code>,
     * <code>goldenDeckTopBack</code> and <code>resourceDeckTopBack</code> provided.
     *
     * @param faceUpCards         the four cards placed face up on the <code>ClientBoard</code>.
     * @param commonObjectives    the two <code>commonObjectives</code> of the game.
     * @param goldenDeckTopBack   the back of the first golden card found in the golden deck.
     * @param resourceDeckTopBack the back of the first resource card found in the resource deck.
     */
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

    public void replaceFaceUpCard(ClientCard faceUpCard, int positionFaceUp) {
        this.faceUpCards.set(positionFaceUp, faceUpCard);
    }

    public boolean isGoldenDeckEmpty() {
        return goldenDeckTopBack == null;
    }

    public boolean isResourceDeckEmpty() {
        return resourceDeckTopBack == null;
    }

}
