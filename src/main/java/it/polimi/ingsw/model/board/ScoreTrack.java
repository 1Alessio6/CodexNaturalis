package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.*;

import java.util.*;

public class ScoreTrack {
    private ArrayList<InterfacePlayerScoreTrack> players; //final??

    //constructor

    //we have to decide if initialize the scoretrack with an array passed as parameter
    public ScoreTrack(){

    }

    //getter methods
    public ArrayList<InterfacePlayerScoreTrack> getPlayers() {
        return players;
    }

    //setter methods

    public void setPlayers(ArrayList<InterfacePlayerScoreTrack> players) {
        this.players = players;
    }

    //other methods

    void updatePlayer(Player currentPlayer){

    }
}
