package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;

import java.util.*;


public class Player implements InterfacePlayerScoreTrack{

    //attributes

    private ArrayList<Card> cards = new ArrayList<>();
    private String username;
    private Playground playground;
    private boolean isActive;
    private Color colour;
    private int player_points;
    private boolean networkStatus;
    private List<ObjectiveCard> objectiveCard = new ArrayList<>();

    @Override
    public int getPoints() {
        return player_points;
    }

    //methods

    public String getUsername() {
        return username;
    }

    public void addCard(Card card){cards.add(card);}

    public void addCard(ObjectiveCard card){objectiveCard.add(card);}

    public void addPoints(int points){ player_points+=points;}

    public void placeCard(Front front, Position position){

    } //temporarily empty

    public void placeCard(Back back, Position position){

    } //temporarily empty

    public void placeObjectiveCard(ObjectiveCard objectiveCard){

    }

    public ArrayList<Position> getAvailablePositions(){
        ArrayList<Position> positions = new ArrayList<>();   //I've added an array to save the positions where the selected card can be positioned
        return positions;
    }

    public void setStatus(boolean networkStatus){
        this.networkStatus = networkStatus;
    }
}

