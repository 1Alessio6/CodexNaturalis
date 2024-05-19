package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.network.VirtualView;

import java.rmi.RemoteException;
import java.util.*;

public class HeartBeat {
    private Map<String, Timer> timers;
    private PingReceiver pingReceiver;
    private Map<String, PingSender> pingSenders;
    private Timer timerToNotifyPresence;
    private int delay;
    private static int defaultDelay = 10000;

    public HeartBeat(PingReceiver pingReceiver) {
        this.timers = new HashMap<>();
        this.pingReceiver = pingReceiver;
        this.delay = defaultDelay;
        this.pingSenders = new HashMap<>();
        this.timerToNotifyPresence = new Timer();
        this.timerToNotifyPresence.schedule(new TimerTask() {
            @Override
            public void run() {
                sendPing();
            }
        }, delay);
    }

    private synchronized void sendPing() {
        List<String> disconnectedSenders = new ArrayList<>();
        for (Map.Entry<String, PingSender> entry : pingSenders.entrySet()) {
            try {
                entry.getValue().sendPing("server");
            } catch (RemoteException e) {
                disconnectedSenders.add(entry.getKey());
            }
        }

        for (String disconnectedSender : disconnectedSenders) {
            timers.remove(disconnectedSender);
            pingSenders.remove(disconnectedSender);
            pingReceiver.handleUnresponsiveness(disconnectedSender);
        }
    }

    public synchronized void setDelay(int newDelay) {
        delay = newDelay;
    }

    public synchronized void addTimerFor(String user, PingSender pingSender) {
        Timer timer = new Timer();
        timer.schedule(
                new UnresponsivenessHandler(pingReceiver, user),
                delay
        );
        timers.put(user, new Timer());
        pingSenders.put(user, pingSender);
    }

    public synchronized void removeTimerFor(String user) {
        if (!timers.containsKey(user)) {
            return;
        }

        timers.get(user).cancel();
        timers.remove(user);
        pingSenders.remove(user);
    }

    public synchronized void registerResponse(String user) {
        Timer timer = timers.get(user);
        timer.cancel();
        timer.schedule(
                new UnresponsivenessHandler(pingReceiver, user),
                delay
        );
    }
}
