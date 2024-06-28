package it.polimi.ingsw.model.lobby;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.rmi.ClientRMI;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.rmi.RMIHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {
    Server server;
    Lobby lobby;

    // could have been a socket handler. RMI as example
    private RMIHandler createRMIHandler (String username) {
        return new RMIHandler(server, new ClientRMI("127.0.0.1"), username);
    }

    @BeforeEach
    void setUp() {
        lobby = new Lobby();
    }

    @Test
    void add() {
        // N.B. manually, client handler and user can have different username. This is handled in Server

        // empty username isn't allowed
        String invalid = "";
        assertThrows(InvalidUsernameException.class, () -> lobby.add(invalid,
                createRMIHandler(invalid)));
        assertEquals(0, lobby.getPlayersInLobby().size());

        // add creator
        assertDoesNotThrow(() -> lobby.add("creator",
                createRMIHandler("creator")));

        for (int i = 0; i < 3; i++) { // max number of players - creator
            String username = "user" + i;
            assertDoesNotThrow(() -> lobby.add(username,
                    createRMIHandler(invalid)));
        }
        // full lobby
        assertThrows(FullLobbyException.class, () -> lobby.add("pippo",
                createRMIHandler("pippo")));

        // already connected
        String alreadyConnected = lobby.getPlayersInLobby().getFirst();
        assertThrows(InvalidUsernameException.class, () -> lobby.add(alreadyConnected,
                createRMIHandler(alreadyConnected)));
    }

    @Test
    void remove() {
        // start from full lobby
        add();

        assertThrows(InvalidUsernameException.class, () -> lobby.remove("random"));
        // first is creator: remove one of the others
        assertDoesNotThrow(() -> lobby.remove(lobby.getPlayersInLobby().getLast()));

        // removing creator removes everyone
        assertDoesNotThrow(() -> lobby.remove("creator"));
        assert(lobby.getPlayersInLobby().isEmpty());

    }
}