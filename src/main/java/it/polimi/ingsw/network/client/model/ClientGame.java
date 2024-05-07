package it.polimi.ingsw.network.client.model;

import java.util.List;

public class ClientGame {
    public ClientPlayer player;

    public ClientPhase currentPhase;

    public List<ClientPlayer> otherPlayers;

    public int[] hiddenObjectivesID; //max 2 and cannot be into ClientPlayer class because it would be visible to all the game partecipants

    public ClientBoard clientBoard;

}
