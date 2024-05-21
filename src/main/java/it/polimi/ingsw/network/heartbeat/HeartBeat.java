package it.polimi.ingsw.network.heartbeat;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class HeartBeat implements Serializable {
    private final HeartBeatHandler heartBeatHandler;
    private HeartBeatListener heartBeatListener;
    private Timer timerForReply;
    private final Timer timerToNotifyPresence;
    private int period;
    private final static int DEF_PERIOD_DELAY = 10000;
    private String sender;

    public HeartBeat(HeartBeatHandler heartBeatHandler, HeartBeatListener heartBeatListener) {
        this.timerForReply = new Timer();
        this.heartBeatHandler = heartBeatHandler;
        this.heartBeatListener = heartBeatListener;
        this.period = DEF_PERIOD_DELAY;
        this.timerToNotifyPresence = new Timer();
    }

    public void startPing(String handlerName) {
        this.sender = handlerName;
        this.timerToNotifyPresence.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        sendPing();
                    }
                }, 0, period);
    }

    private synchronized void sendPing() {
        System.out.println("Try to sending ping");
        try {
            timerForReply = new Timer();
            timerForReply.schedule(
                    new UnresponsivenessHandler(heartBeatHandler),
                    period / 2
            );
            heartBeatListener.receivePing(sender);
        } catch (RemoteException e) {
            System.out.println("Server disconnected while client was sending ping");
            heartBeatHandler.handleUnresponsiveness();
        }
    }

    public synchronized void setPeriod(int newDelay) {
        period = newDelay;
    }

    public synchronized void setHeartBeatListener(HeartBeatListener heartBeatListener) {
        this.heartBeatListener = heartBeatListener;
    }

    public synchronized void registerResponse() {
        System.out.println("Server is alive");
        timerForReply.cancel();
    }

    public synchronized void shutDown() {
        timerForReply.cancel();
        timerToNotifyPresence.cancel();
    }
}
