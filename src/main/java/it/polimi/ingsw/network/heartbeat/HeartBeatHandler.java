package it.polimi.ingsw.network.heartbeat;

public interface HeartBeatHandler {
    void handleUnresponsiveness(String name);
}
