package it.polimi.ingsw.network.client.socket;

import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.heartbeat.HeartBeat;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;


/**
 * The ClientSocket defines the methods to handle a Socket connection and inherits from Client the methods to update the client's model and view.
 */
public class ClientSocket extends Client implements HeartBeatHandler {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private HeartBeat heartBeat;

    public ClientSocket() {}

    @Override
    public VirtualView getInstanceForTheServer() {
        return this;
    }

    /**
     * Closes input, output and socket.
     */
    private void closeResources() {
        try {
            this.socket.close();
            this.in.close();
            this.out.close();
        } catch (Exception ignored) {

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualServer bindServer(String ip, Integer port) throws UnReachableServerException {
        try {
            this.socket = new Socket(ip, port);
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ServerHandler server = new ServerHandler(this, in, out);
            heartBeat = new HeartBeat(this, "unknown", server, "server");
            server.start();
            return server;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + ip);
            closeResources();
            throw new UnReachableServerException(e.getMessage());
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + ip);
            closeResources();
            throw new UnReachableServerException(e.getMessage());
        }
    }

    @Override
    protected void terminateConnection() {
        isConnected = false;
        heartBeat.terminate();
        closeResources();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resultOfLogin(boolean accepted, String username, String details) {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                System.err.println("I'm (" + username + ") already connected");
                return;
            }

            if (accepted) {
                controller.setMainPlayerUsername(username);
                heartBeat.setHandlerName(username);
                heartBeat.startHeartBeat();
                isConnected = true;
            } else {
                clientView.showInvalidLogin(details);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        heartBeat.registerMessage(ping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                terminateConnection();
                clientView.showConnectionLost();
            }
        }
    }
}
