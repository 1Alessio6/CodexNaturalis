package it.polimi.ingsw.model.listenerhandler;

import it.polimi.ingsw.model.notifier.Notifier;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListenerHandler<ListenerType> {
    private final Map<String, ListenerType> idToListener;
    private final Map<String, ExecutorService> idToExecutors;

    public ListenerHandler() {
        idToListener = new HashMap<>();
        idToExecutors = new HashMap<>();
    }

    public synchronized void add(String username, ListenerType listener) {
        idToListener.put(username, listener);
        idToExecutors.put(username, Executors.newSingleThreadExecutor());
    }

    private void removeUser(String username) {
        idToListener.remove(username);
        ExecutorService executorService = idToExecutors.remove(username);
        executorService.shutdown();
    }

    public synchronized void remove(String username) {
        removeUser(username);
    }

    public synchronized ListenerType get(String username) {
        return idToListener.get(username);
    }

    public synchronized List<String> getIds() {
        return new ArrayList<>(idToListener.keySet());
    }

    public synchronized int getNumListener() {
        return idToListener.size();
    }

    public synchronized void notify(String username, Notifier<ListenerType> notifier) {
        if (!idToListener.containsKey(username))
            return;
        ListenerType toNotify = idToListener.get(username);
        ExecutorService executor = idToExecutors.get(username);
        executor.submit(() -> sendNotification(username, toNotify, notifier));
    }

    private void sendNotification(String id, ListenerType recipient, Notifier<ListenerType> notifier) {
        try {
            notifier.sendUpdate(recipient);
        } catch (RemoteException e) {
            System.err.println("The player " + id + " has been disconnected while sending the notification");
            removeUser(id);
        }
    }

    public synchronized void notifyBroadcast(Notifier<ListenerType> notifier) {
        idToListener.forEach((id, listener) -> idToExecutors.get(id).submit(
                () -> sendNotification(id, listener, notifier)));
    }

    public synchronized void clear() {
        Set<String> users = idToListener.keySet();

        for (String user : users) {
            removeUser(user);
        }
    }
}
