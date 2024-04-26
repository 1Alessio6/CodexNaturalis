package it.polimi.ingsw.model.lobby;

public class AlreadyInLobbyException extends Exception{
    public AlreadyInLobbyException() {
        super("Already In Lobby");
    }

    public AlreadyInLobbyException(String message) {
        super(message);
    }
}
