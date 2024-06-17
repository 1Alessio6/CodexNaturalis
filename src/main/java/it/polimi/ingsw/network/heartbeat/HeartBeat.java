package it.polimi.ingsw.network.heartbeat;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class HeartBeat extends TimerTask {
    private String handlerName;
    private String listenerName;
    private final HeartBeatHandler heartBeatHandler;
    private HeartBeatListener heartBeatListener;
    private static final int MAX_DELTA = 3;
    private static final int DEF_HEART_BEAT_PERIOD = 1000; // ms
    private int heartBeatPeriod;
    private int delay;
    private final Timer timer;
    private AtomicInteger mostRecentReceivedId;
    private Integer lastSentId;
    private boolean isActive;


    public HeartBeat(HeartBeatHandler heartBeatHandler, String handlerName, HeartBeatListener heartBeatListener, String listenerName) {
        this.heartBeatHandler = heartBeatHandler;
        this.handlerName = handlerName;
        this.heartBeatListener = heartBeatListener;
        this.listenerName = listenerName;
        this.mostRecentReceivedId = new AtomicInteger();
        this.heartBeatPeriod = DEF_HEART_BEAT_PERIOD;
        this.delay = 0;
        this.lastSentId = 0;
        timer = new Timer();
        isActive = new AtomicBoolean(true);
    }

    public synchronized void setHeartBeatPeriod(int heartBeatPeriod) {
        if (heartBeatPeriod < 0) {
            throw new IllegalArgumentException("Invalid heartbeat period: negative value not allowed");
        }
        this.heartBeatPeriod = heartBeatPeriod;
    }

    public synchronized void setDelay(int delay) {
        this.delay = delay;
    }

    public synchronized void setHandlerName(String name){
        this.handlerName = name;
    }

    public synchronized void startHeartBeat() {
        isActive = true;
        timer.scheduleAtFixedRate(this, delay, heartBeatPeriod);
    }

 //   public synchronized void setListener(HeartBeatListener heartBeatListener, String listenerName) {
 //       this.heartBeatListener = heartBeatListener;
 //       this.listenerName = listenerName;
 //   }

    public void registerMessage(HeartBeatMessage pong) {
        mostRecentReceivedId.set(pong.getId());
    }

    @Override
    public synchronized void run() {
        lastSentId += 1;
        HeartBeatMessage ping = new HeartBeatMessage(handlerName, lastSentId);
        int delta = ping.getId() - mostRecentReceivedId.get();

        if (!isActive.get()) {
            return;
        }

        if (delta <= MAX_DELTA) {
            try {
                heartBeatListener.receivePing(ping);
            } catch (RemoteException e) {
                System.err.println("Remote exception in Heartbeat: the listener has disconnected for this reason: " + e.getMessage());
                heartBeatHandler.handleUnresponsiveness(listenerName);
            }
        } else {
            heartBeatHandler.handleUnresponsiveness(listenerName);
        }
    }

    /**
     * Terminates the heartbeat.
     * The method is invoked whenever a disconnection occurs.
     * All references to external objects become invalid to avoid running task to interfere with the state of such objects.
     */
    public void terminate() {
        isActive.set(false);
        synchronized (this) {
            timer.cancel();
        }
    }

    public synchronized boolean isActive() {
        return isActive;
    }
}