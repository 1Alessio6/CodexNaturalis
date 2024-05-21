package it.polimi.ingsw.network.heartbeat;

import java.util.TimerTask;

class UnresponsivenessHandler extends TimerTask {
    private HeartBeatHandler heartBeatHandler;
    private String user;

    public UnresponsivenessHandler(HeartBeatHandler heartBeatHandler, String user) {
        this.heartBeatHandler = heartBeatHandler;
        this.user = user;
    }

    @Override
    public void run() {
        heartBeatHandler.handleUnresponsiveness(user);
    }
}

