package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;

import java.util.ArrayList;
import java.util.Map;

public class ClientPlayer {

    private final String username;

    private ClientPlayground playground;

    private PlayerColor color;

    private boolean isConnected;

    private boolean isCurrentPlayer;

    private int starterFrontID;

    private int starterBackID; //todo check if it's needed

    private int[] playerCardsFrontID; //max 3

    private int[] PlayerCardsBackID;

    private Map<Integer, Map<Symbol, Integer>> goldenFrontRequirements; //todo could be changed into an array list of map


    public ClientPlayer(String username) {
        this.username = username;
    }

    //getter method needed for the view

    public int getAmountResource(Symbol s){
        return this.getPlayground().getResources().get(s);
    }

    public boolean isGoldenFront(int frontID){
        return this.goldenFrontRequirements.containsKey(frontID);
    }

    public Map<Symbol, Integer> getGoldenFrontRequirements(int frontID){
        assert(isGoldenFront(frontID));
        return this.goldenFrontRequirements.get(frontID);
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    public void setIsCurrentPlayer(boolean currentPlayer) {
        isCurrentPlayer = currentPlayer;
    }

    public ClientPlayground getPlayground() {
        return playground;
    }

    public String getUsername() {
        return username;
    }

    public PlayerColor getColor() {
        return color;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int[] getPlayerCardsBackID() {
        return PlayerCardsBackID;
    }

    public int[] getPlayerCardsFrontID() {
        return playerCardsFrontID;
    }

    public int getStarterFrontID() {
        return starterFrontID;
    }

    public int getStarterBackID() {
        return starterFrontID;
    }


    //setter methods

    public void setNetworkStatus(boolean networkStatus) {
        this.isConnected = networkStatus;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public void setStarterFrontID(int starterFrontID) {
        this.starterFrontID = starterFrontID;
    }

    public void setStarterBackID(int starterBackID) {
        this.starterBackID = starterBackID;
    }
}
