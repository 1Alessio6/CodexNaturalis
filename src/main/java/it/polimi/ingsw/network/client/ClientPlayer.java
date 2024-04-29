package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.Color.PlayerColor;

import java.util.List;

public class ClientPlayer {

    private final String username;

    private ClientPlayground playground;

    private PlayerColor color;

    private boolean isConnected;

    private int starterCard; //todo check if it's needed

    private int[] cards; //max 3

    private int[] hiddenObjectives;


    public ClientPlayer(String username) {
        this.username = username;
    }

    //getter method needed for the view
    public String getUsername() {
        return username;
    }

    public PlayerColor getColor() {
        return color;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int[] getCards() {
        return cards;
    }

    public int[] getHiddenObjectives() {
        return hiddenObjectives;
    }

    public int getStarterCard() {
        return starterCard;
    }
}
