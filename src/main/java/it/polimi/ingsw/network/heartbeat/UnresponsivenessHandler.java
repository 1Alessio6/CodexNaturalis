package it.polimi.ingsw.network.heartbeat;

import java.util.TimerTask;

class UnresponsivenessHandler extends TimerTask {
    private HeartBeatHandler heartBeatHandler;

    public UnresponsivenessHandler(HeartBeatHandler heartBeatHandler) {
        this.heartBeatHandler = heartBeatHandler;
    }

    @Override
    public void run() {
        heartBeatHandler.handleUnresponsiveness();
    }
}

