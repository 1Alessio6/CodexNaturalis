package it.polimi.ingsw.network.heartbeat;

public interface PingReceiver {
    void handleUnresponsiveness(String unresponsiveUser);
}
