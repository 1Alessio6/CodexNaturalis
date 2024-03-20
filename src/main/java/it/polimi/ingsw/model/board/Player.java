package it.polimi.ingsw.model.board;
import javax.smartcardio.Card;
import java.util.*;

public class Player implements InterfacePlayerScoreTrack{

    //attributes

    private ArrayList<Card> cards=new ArrayList<>();
    private String id;
    private Playground playground;
    private boolean isActive;
    private Colour colour;
    private int player_points;
    private boolean network_status;
    private List<ObjectiveCard> objectiveCard=new List<ObjectiveCard>();

    @Override
    public int getPoints() {
        return player_points;
    }

    //methods

    public void addCard(Card card){

    }

    public void addPoints(int points){

    }

    public void placeCard(Front front, Position position){

    } //temporarily empty

    public void placeCard(Back back, Position position){

    } //temporarily empty

    public void placeObjectiveCard(ObjectiveCard objectiveCard){

    }

    public ArrayList<Position> getAvailablePositions(){
        private ArrayList<Position> positions=new ArrayList<>();   //I've added an array to save the positions where the selected card can be positioned


    }

    public void setStatus(boolean network_status){
        this.network_status=network_status;
    }



}

