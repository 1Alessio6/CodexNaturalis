package it.polimi.ingsw.network.heartbeat;

import java.util.TimerTask;

class UnresponsivenessHandler extends TimerTask {
    private PingReceiver pingReceiver;
    private String user;

    public UnresponsivenessHandler(PingReceiver pingReceiver, String user) {
        this.pingReceiver = pingReceiver;
        this.user = user;
    }

    @Override
    public void run() {
        pingReceiver.handleUnresponsiveness(user);
    }
}

