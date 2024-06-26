package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.heartbeat.HeartBeat;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;
import it.polimi.ingsw.network.server.rmi.ServerRMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The ClientRMI defines the methods to handle an RMI connection and inherits from Client the methods to update the client's model and view.
 */
public class ClientRMI extends Client implements HeartBeatHandler {
    private HeartBeat heartBeat;
    private VirtualView stub;

    public ClientRMI() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualServer bindServer(String ip, Integer port) throws UnReachableServerException {
        String serverName = ServerRMI.getServerName();
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            VirtualServer server = (VirtualServer) registry.lookup(serverName);
            heartBeat = new HeartBeat(this, "unknown", server, "server");
            return server;
        } catch (RemoteException | NotBoundException e) {
            throw new UnReachableServerException(e.getMessage());
        }
    }

    @Override
    public VirtualView getInstanceForTheServer() throws RemoteException {
        if (stub == null) {
            stub = (VirtualView) UnicastRemoteObject.exportObject(this, 0);
        }
        return stub;
    }

    @Override
    protected void terminateConnection() {
        if (heartBeat != null) {
            heartBeat.terminate();
        }
        isConnected = false;
    }

    @Override
    public void resultOfLogin(boolean accepted, String username, String details) throws RemoteException {
        System.err.println("Received result of login");
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                System.err.println("I'm (" + username + ") already connected");
                return;
            }

            System.err.println(" which belongs to me");
            System.err.println(username + " is connected");
            if (accepted) {
                //System.out.println(username + " has been accepted");
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
    public void handleUnresponsiveness(String unresponsiveListener) {
        synchronized (lockOnNetworkStatus) {
            if (isConnected) {
                terminateConnection();
                clientView.showConnectionLost();
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

}
