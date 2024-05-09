package it.polimi.ingsw.network.client.model.board;

import java.util.ArrayList;


public class ClientBoard {

    private int[] faceUpCardsFrontID; //max 4

    private int[] faceUpCardsBackID;

    private ArrayList<Integer> goldenDeck; //contains only backID numbers

    private ArrayList<Integer> resourceDeck; //contains only backID numbers

    private int[] commonObjectives;

    public ClientBoard() {
        faceUpCardsFrontID = new int[4];
        faceUpCardsBackID = new int[4];
        commonObjectives = new int[2];
        goldenDeck = new ArrayList<>();
        resourceDeck = new ArrayList<>();
    }

    //getter methods

    public int[] getFaceUpCards() {return faceUpCardsFrontID;}

    public ArrayList<Integer> getGoldenDeck() {return goldenDeck;}

    public ArrayList<Integer> getResourceDeck() {return resourceDeck;}

    public int[] getCommonObjectives() {return commonObjectives;}

    //setter methods


    public void setCommonObjectives(int[] commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    public void setFaceUpCards(int[] faceUpCards) {
        this.faceUpCardsFrontID = faceUpCards;
    }

    public void addGoldenBackID(int goldenBackID) {
        goldenDeck.add(goldenBackID);
    }

    public void addResourceBackID(int resourceBackID) {
        resourceDeck.add(resourceBackID);
    }

    public void addFaceUpCards(int newFrontFaceUp, int newBackFaceUp, int positionFaceUp) {
        this.faceUpCardsFrontID[positionFaceUp] = newFrontFaceUp;
        this.faceUpCardsBackID[positionFaceUp] = newBackFaceUp;
    }
}
