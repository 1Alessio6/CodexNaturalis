package it.polimi.ingsw.model.listenerhandler;

import it.polimi.ingsw.model.notifier.Notifier;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListenerHandler<ListenerType> {
    private final ConcurrentHashMap<String, ListenerType> idToListener;
    private final ExecutorService executor;

    public ListenerHandler() {
        idToListener = new ConcurrentHashMap<>();
        executor = Executors.newSingleThreadExecutor();
    }

    public void add(String username, ListenerType listener) {
        idToListener.put(username, listener);
    }

    public void remove(String username) {
        idToListener.remove(username);
    }

    public ListenerType get(String username) {
        return idToListener.get(username);
    }

    public List<String> getIds() {
        return new ArrayList<>(idToListener.keySet());
    }

    public int getNumListener() {
        return idToListener.size();
    }

    public void notify(String username, Notifier<ListenerType> notifier) {
        if (!idToListener.containsKey(username)) return;
        ListenerType toNotify = idToListener.get(username);
        executor.submit(() -> sendNotification(username, toNotify, notifier));
    }

    private void sendNotification(String id, ListenerType recipient, Notifier<ListenerType> notifier) {
        try {
            notifier.sendUpdate(recipient);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Disconnected player" + id);
            idToListener.remove(id);
        }
    }

    public void notifyBroadcast(Notifier<ListenerType> notifier) {
        idToListener.forEach((id, listener) -> sendNotification(id, listener, notifier));
    }

    public void clear() {
        idToListener.clear();
    }
}
