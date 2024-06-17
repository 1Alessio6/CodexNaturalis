package it.polimi.ingsw.model.listenerhandler;

import it.polimi.ingsw.model.notifier.Notifier;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ListenerHandler adds, removes and notifies players
 */
public class ListenerHandler<ListenerType> {
    private final Map<String, ListenerType> idToListener;
    private final Map<String, ExecutorService> idToExecutors;

    /**
     * Constructs a Listener Handler
     */
    public ListenerHandler() {
        idToListener = new HashMap<>();
        idToExecutors = new HashMap<>();
    }

    /**
     * Adds a player to the map of listeners and assigns an executor to it
     *
     * @param username of the player
     * @param listener type
     */
    public synchronized void add(String username, ListenerType listener) {
        idToListener.put(username, listener);
        idToExecutors.put(username, Executors.newSingleThreadExecutor());
    }

    /**
     * Removes a player from the map of listeners and shuts down its executor
     *
     * @param username of the player
     */
    private void removeUser(String username) {
        idToListener.remove(username);
        ExecutorService executorService = idToExecutors.remove(username);
        executorService.shutdown();
    }

    /**
     * Removes a player synchronously
     *
     * @param username of the player
     */
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

    /**
     * Notifies a player
     *
     * @param username of the player
     * @param notifier with the players
     */
    public synchronized void notify(String username, Notifier<ListenerType> notifier) {
        if (!idToListener.containsKey(username))
            return;
        ListenerType toNotify = idToListener.get(username);
        ExecutorService executor = idToExecutors.get(username);
        executor.submit(() -> sendNotification(username, toNotify, notifier));
    }

    /**
     * Sends a notification to a certain player
     *
     * @param id        of the player
     * @param recipient to notify
     * @param notifier  with the players
     */
    private synchronized void sendNotification(String id, ListenerType recipient, Notifier<ListenerType> notifier) {
        try {
            notifier.sendUpdate(recipient);
        } catch (RemoteException e) {
            System.err.println("The player " + id + " has been disconnected while sending the notification");
            removeUser(id);
        }
    }

    /**
     * Notifies all players in the game
     *
     * @param notifier with players
     */
    public synchronized void notifyBroadcast(Notifier<ListenerType> notifier) {
        idToListener.forEach((id, listener) -> idToExecutors.get(id).submit(
                () -> sendNotification(id, listener, notifier)));
    }

    /**
     * Clear all players from the <code>Listener Handler</code>
     */
    public synchronized void clear() {
        Set<String> users = idToListener.keySet();

        for (String user : users) {
            removeUser(user);
        }
    }
}
