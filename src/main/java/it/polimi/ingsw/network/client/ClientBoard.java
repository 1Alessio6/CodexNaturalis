package it.polimi.ingsw.network.client;

import java.util.ArrayList;


public class ClientBoard {

    private int[] faceUpCards; //max 4

    private ArrayList<Integer> goldenDeck;

    private ArrayList<Integer> resourceDeck;
    private int[] commonObjectives;

    public ClientBoard() {
        faceUpCards = new int[4];
        commonObjectives = new int[2];
        goldenDeck = new ArrayList<>();
        resourceDeck = new ArrayList<>();
    }

    //getter methods

    public int[] getFaceUpCards(){return faceUpCards;}

    public ArrayList<Integer> getGoldenDeck(){return goldenDeck;}

    public ArrayList<Integer> getResourceDeck(){return resourceDeck;}

    public int[] getCommonObjectives(){return commonObjectives;}

}
