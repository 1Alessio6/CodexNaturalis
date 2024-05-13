package it.polimi.ingsw.model.lobby;

public class FullLobbyException extends Exception {
    public FullLobbyException() {
        super("Game hasn't started yet, but lobby is already full");
    }
}
