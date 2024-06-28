package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.rmi.ClientRMI;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.rmi.RMIHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller controller;
    Server server; // just as placeholder

    // could have been a socket handler. RMI as example
    private RMIHandler createRMIHandler (String username) {
        return new RMIHandler(server, new ClientRMI("127.0.0.1"), username);
    }

    private void addPlayersInLobby(List<String> usernames) {
        for (String username : usernames) {
            assertTrue(controller.handleConnection(username, createRMIHandler(username)));
        }
    }

    @BeforeEach
    void setUp() {
        controller = new Controller();
    }

    @Test
    void handleConnection() {
        // first user allowed
        assertTrue(controller.handleConnection("first", createRMIHandler("first")));
        // same username
        assertFalse(controller.handleConnection("first", createRMIHandler("first")));
        // invalid username
        assertFalse(controller.handleConnection("", createRMIHandler("")));

        List<String> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            users.add("user" + i);
        }

        addPlayersInLobby(users);

        // max users reached
        assertFalse(controller.handleConnection("ciaone", createRMIHandler("ciaone")));

        // set lobby size
        int lobbySize = 3;
        controller.setPlayersNumber("first", lobbySize);

        // game is already started, as max lobby player number is reached
        assertFalse(controller.handleConnection("random", createRMIHandler("random")));
    }

    @Test
    void handleDisconnection_lobby() {
        List<String> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            users.add("user" + i);
        }

        addPlayersInLobby(users);

        // invalid username
        controller.handleDisconnection("");

        // disconnect a player but the creator
        controller.handleDisconnection("user0");
        // after this disconnection it should be possible to connect again
        assertTrue(controller.handleConnection("random", createRMIHandler("random")));

        // disconnecting creator without setting lobby size will reset lobby
        controller.handleDisconnection("user0");
        assertTrue(controller.handleConnection("user1", createRMIHandler("user1")));
    }

    @Test
    void handleDisconnection_game() {
        List<String> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            users.add("user" + i);
        }
        addPlayersInLobby(users);

        controller.setPlayersNumber("user0", 4);
        assertTrue(controller.handleConnection("random", createRMIHandler("random")));
        // from now game should have started

        controller.handleDisconnection("user0");
    }
}