package it.polimi.ingsw.network.heartbeat;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HeartBeat extends TimerTask {
    private final String handlerName;
    private String listenerName;
    private final HeartBeatHandler heartBeatHandler;
    private HeartBeatListener heartBeatListener;
    private static final int MAX_DELTA = 7;
    private static final int DEF_HEART_BEAT_PERIOD = 1000; // ms
    private int heartBeatPeriod;
    private int delay;
    private Timer timer;
    private AtomicInteger mostRecentReceivedId;
    private Integer lastSentId;


    public HeartBeat(HeartBeatHandler heartBeatHandler, String handlerName, HeartBeatListener heartBeatListener, String listenerName) {
        this.heartBeatHandler = heartBeatHandler;
        this.handlerName = handlerName;
        this.heartBeatListener = heartBeatListener;
        this.listenerName = listenerName;
        this.mostRecentReceivedId = new AtomicInteger();
        this.heartBeatPeriod = DEF_HEART_BEAT_PERIOD;
        this.delay = 0;
        this.lastSentId = 0;
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

    public synchronized void startHeartBeat() {
        timer = new Timer();
        timer.scheduleAtFixedRate(this, delay, heartBeatPeriod);
    }

 //   public synchronized void setListener(HeartBeatListener heartBeatListener, String listenerName) {
 //       this.heartBeatListener = heartBeatListener;
 //       this.listenerName = listenerName;
 //   }

    public void registerMessage(HeartBeatMessage pong) {
        mostRecentReceivedId.set(pong.getId());
        System.out.println("Most recent ping id is " + mostRecentReceivedId);
    }

    @Override
    public synchronized void run() {
        lastSentId += 1;
        HeartBeatMessage ping = new HeartBeatMessage(handlerName, lastSentId);
        int delta = ping.getId() - mostRecentReceivedId.get();
        System.out.println("Delta is " + delta);
        if (delta <= MAX_DELTA) {
            try {
                heartBeatListener.receivePing(ping);
            } catch (RemoteException e) {
                System.err.println("Remote exception in Heartbeat: the listener has disconnected for this reason: " + e.getMessage());
                try {
                    heartBeatHandler.handleUnresponsiveness(listenerName);
                } catch (RemoteException ignored) {
                    System.err.println("Exception should not be thrown: heart beat handler is local");
                }
            }
        } else {
            try {
                heartBeatHandler.handleUnresponsiveness(listenerName);
            } catch (RemoteException ignored) {
                System.err.println("Exception should not be thrown: heart beat handler is local");
            }
        }
    }

    public synchronized void terminate() {
        timer.cancel();
    }
}