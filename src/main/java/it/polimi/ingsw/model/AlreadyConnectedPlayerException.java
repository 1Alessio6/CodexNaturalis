package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

public class AlreadyConnectedPlayer extends Exception{
    public AlreadyConnectedPlayer(Player player) {super("The player is already connected");}

}
