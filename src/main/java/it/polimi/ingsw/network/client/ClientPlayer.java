package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Color.PlayerColor;

public class ClientPlayer {

    private final String username;

    private ClientPlayground playground;

    private PlayerColor color;

    private boolean isConnected;

    private int starterFrontID;

    private int starterBackID; //todo check if it's needed

    private int[] frontID; //max 3

    private int[] backID;


    public ClientPlayer(String username) {
        this.username = username;
    }

    //getter method needed for the view

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

    public int[] getBackID() {
        return backID;
    }

    public int[] getFrontID() {
        return frontID;
    }

    public int getStarterFrontID() {
        return starterFrontID;
    }

    public int getStarterBackID() {
        return starterFrontID;
    }


    //setter methods

    public void setNetworkStatus(boolean networkStatus){this.isConnected=networkStatus;}

    public void setColor(PlayerColor color){this.color=color;}

    public void setStarterFrontID(int starterFrontID){this.starterFrontID=starterFrontID;}

    public void setStarterBackID(int starterBackID){this.starterBackID=starterBackID;}
}
