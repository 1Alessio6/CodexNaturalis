package it.polimi.ingsw.model.notifier;

public interface Notifier<ReceiverType> {
    void sendUpdate(ReceiverType receiver);
}
