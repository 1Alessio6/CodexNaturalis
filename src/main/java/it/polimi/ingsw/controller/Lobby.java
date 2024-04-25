package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Color;

import java.util.*;

public class Lobby {
    private List<String> users;
    private boolean isStarted;
    private final int gameSize;
    private final int MAX_NUMBER = 4;
    private final int MIN_NUMBER = 2;
    private List<Color> remainColors;

    protected Lobby(String creator, int size) throws IllegalArgumentException {
        if (size > MAX_NUMBER || size < MIN_NUMBER)
            throw new IllegalArgumentException("Number of players should be between " + MIN_NUMBER + " and " + MAX_NUMBER);

        this.gameSize = size;
        users = new ArrayList<>(size);
        isStarted = false;

        users.add(creator);
    }

    protected void addPlayer (String username, Color color) throws IndexOutOfBoundsException{
        if (users.size() == this.gameSize)
            throw new IndexOutOfBoundsException("Already reached " + gameSize + "players!");

        users.add(username);
    }

    protected Game createGame() throws IllegalStateException {
        if (users.size() < this.gameSize) {
            int playersLeft = this.gameSize - this.users.size();

            throw new IllegalStateException("Not enough players: " + playersLeft + " left");
        }

        return new Game(this.users);
    }

    public List<Color> getRemainColors(){
        return remainColors;
    }
}
