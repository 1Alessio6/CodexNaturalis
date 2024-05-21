package it.polimi.ingsw.network.heartbeat;

import java.rmi.RemoteException;
import java.util.*;

public class HeartBeat {
    private Map<String, Timer> timersForReply;
    private HeartBeatHandler heartBeatHandler;
    // the senders waiting for the ping from the PingReceiver
    private Map<String, HeartBeatListener> nameToListener;
    private Timer timerToNotifyPresence;
    private int delay;
    private static int defaultDelay = 10000;
    private String sender;

    public HeartBeat(HeartBeatHandler heartBeatHandler) {
        this.timersForReply = new HashMap<>();
        this.heartBeatHandler = heartBeatHandler;
        this.delay = defaultDelay;
        this.nameToListener = new HashMap<>();
        this.timerToNotifyPresence = new Timer();
    }

    public void startPing(String name) {
        this.sender = name;
        this.timerToNotifyPresence.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        sendPing();
                    }
                }, delay);
    }

    private synchronized void sendPing() {
        List<String> disconnectedListeners = new ArrayList<>();
        for (Map.Entry<String, HeartBeatListener> entry : nameToListener.entrySet()) {
            try {
                entry.getValue().receivePing(sender);
                timersForReply.get(entry.getKey()).schedule(
                        new UnresponsivenessHandler(heartBeatHandler, entry.getKey()),
                        delay
                );
            } catch (RemoteException e) {
                disconnectedListeners.add(entry.getKey());
            }
        }

        for (String disconnectedListener : disconnectedListeners) {
            timersForReply.remove(disconnectedListener);
            nameToListener.remove(disconnectedListener);
            heartBeatHandler.handleUnresponsiveness(disconnectedListener);
        }
    }

    public synchronized void setDelay(int newDelay) {
        delay = newDelay;
    }

    public synchronized void addHeartBeatListener(String user, HeartBeatListener pingSender) {
        Timer timer = new Timer();
        timer.schedule(
                new UnresponsivenessHandler(heartBeatHandler, user),
                delay
        );
        timersForReply.put(user, new Timer());
        nameToListener.put(user, pingSender);
    }

    public synchronized void removeTimerFor(String user) {
        if (!timersForReply.containsKey(user)) {
            return;
        }

        timersForReply.get(user).cancel();
        timersForReply.remove(user);
        nameToListener.remove(user);
    }

    public synchronized void registerResponse(String user) {
        Timer timer = timersForReply.get(user);
        timer.cancel();
        timer.schedule(
                new UnresponsivenessHandler(heartBeatHandler, user),
                delay
        );
    }

    public synchronized void shutDown() {
        for (Timer timer : timersForReply.values()) {
            timer.cancel();
        }
        timerToNotifyPresence.cancel();
    }
}
