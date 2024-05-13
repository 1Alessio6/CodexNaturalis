package it.polimi.ingsw.network.server.socket;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientReceiver {
    final Server server;
    final BufferedReader in;

    public ClientReceiver(Server serverSocket, BufferedReader in) {
        this.server = serverSocket;
        this.in = in;
    }

    public void receive() throws IOException {

    }
}
