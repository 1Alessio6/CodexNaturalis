package it.polimi.ingsw.network.heartbeat;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The HeartBeat class keeps track of the existence of the remote endpoint which can be the server for the client or the client for the server.
 * The HeartBeat must always reflect the state of the connection, that is, it has to be terminated {@link #terminate()} whenever a connection ends.
 */
public class HeartBeat extends Thread {
    private String handlerName;
    private final Object handlerNameLock;
    private String listenerName;
    private HeartBeatHandler heartBeatHandler;
    private HeartBeatListener heartBeatListener;
    private static final int MAX_DELTA = 3; // 3
    private static final int HEART_BEAT_PERIOD = 1000;
    private static final int MAX_DELAY = 5000; // 5000
    private AtomicInteger mostRecentReceivedId;
    private Integer lastSentId;
    private AtomicBoolean isActive;


    /**
     * Constructs the heart beat.
     *
     * @param heartBeatHandler  the local end point of the connection.
     * @param handlerName       the identifier for the handler.
     * @param heartBeatListener the remote end point of the connection.
     * @param listenerName      the identifier for the listener.
     */
    public HeartBeat(HeartBeatHandler heartBeatHandler, String handlerName, HeartBeatListener heartBeatListener, String listenerName) {
        this.heartBeatHandler = heartBeatHandler;
        this.handlerName = handlerName;
        this.heartBeatListener = heartBeatListener;
        this.handlerNameLock = new Object();
        this.listenerName = listenerName;
        this.mostRecentReceivedId = new AtomicInteger();
        this.lastSentId = 0;
        isActive = new AtomicBoolean(true);
    }

    /**
     * Sets the name of the handler.
     *
     * @param name of the handler.
     */
    public void setHandlerName(String name) {
        synchronized (handlerNameLock) {
            this.handlerName = name;
        }
    }

    /**
     * Starts sending the heart beat to the remote end point.
     */
    public void startHeartBeat() {
        this.start();
    }

    /**
     * Registers the arrival of a message from the remote end point.
     *
     * @param ping the message notify the existence of the remote endpoint.
     */
    public void registerMessage(HeartBeatMessage ping) {
        mostRecentReceivedId.set(ping.getId());
    }

    /**
     * Sends the heart beat to the remote reference notifying about the existence of the local end point.
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            lastSentId += 1;
            HeartBeatMessage ping;
            synchronized (handlerNameLock) {
                ping = new HeartBeatMessage(handlerName, lastSentId);
            }
            int delta = ping.getId() - mostRecentReceivedId.get();
            System.err.println("Current delta: " + delta + " for " + handlerName + " to " + listenerName);

            if (delta <= MAX_DELTA) {
                try {
                    Timer timerDelayForSendingPing = new Timer();
                    timerDelayForSendingPing.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("Timer for sending ping of " + listenerName + " has expired");
                            heartBeatHandler.handleUnresponsiveness(listenerName);
                        }
                    }, MAX_DELAY);
                    heartBeatListener.receivePing(ping);
                    timerDelayForSendingPing.cancel();
                } catch (RemoteException e) {
                    System.err.println("REMOTE EXCEPTION in Heartbeat: the listener has disconnected for this reason: " + e.getMessage());
                    heartBeatHandler.handleUnresponsiveness(listenerName);
                }
            } else {
                System.err.println("Delta for " + listenerName + " is too high: last_delta=" + delta);
                heartBeatHandler.handleUnresponsiveness(listenerName);
            }
            try {
                Thread.sleep(HEART_BEAT_PERIOD);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Terminates the heartbeat.
     * The method is invoked whenever a disconnection occurs.
     * All references to external objects become invalid to avoid running task to interfere with the state of such objects.
     */
    public void terminate() {
        System.err.println("Terminate the heartbeat");
        this.interrupt();
        isActive.set(false);
    }

    /**
     * Returns the state of the heartbeat.
     *
     * @return true if the heart beat is valid, false otherwise.
     * The heart beat is valid if it's never terminated.
     */
    public boolean isActive() {
        return isActive.get();
    }
}