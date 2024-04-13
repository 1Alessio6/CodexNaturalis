package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

public class AlreadyConnectedPlayer extends Exception{
    public AlreadyConnectedPlayer(){
        super("The player is already connected");
    }

    public AlreadyConnectedPlayer(Player player) {
        super("The player is already connected");
    }
}
