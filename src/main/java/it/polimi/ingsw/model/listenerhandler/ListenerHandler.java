package it.polimi.ingsw.model.listenerhandler;

import it.polimi.ingsw.model.notifier.Notifier;

import java.util.*;

/**
 * ListenerHandler adds, removes and notifies listeners
 */
public class ListenerHandler<ListenerType> {
    private final Map<String, ListenerType> idToListener;

    /**
     * Constructs a Listener Handler
     */
    public ListenerHandler() {
        idToListener = new HashMap<>();
    }

    /**
     * Adds a player to the map of listeners and assigns an executor to it
     *
     * @param username of the player
     * @param listener type
     */
    public void add(String username, ListenerType listener) {
        idToListener.put(username, listener);
    }

    /**
     * Removes a player from the map of listeners and shuts down its executor
     *
     * @param username of the player
     */
    private void removeUser(String username) {
        idToListener.remove(username);
    }

    /**
     * Removes a listener
     *
     * @param username of the player
     */
    public void remove(String username) {
        removeUser(username);
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

    /**
     * Notifies a player
     *
     * @param username of the player
     * @param notifier with the players
     */
    public void notify(String username, Notifier<ListenerType> notifier) {
        ListenerType toNotify = idToListener.get(username);
        // the result of an automatic draw
        if (toNotify == null) {
            return;
        }
        sendNotification(username, toNotify, notifier);
    }

    /**
     * Sends a notification to a certain player
     *
     * @param id        of the player
     * @param recipient to notify
     * @param notifier  with the players
     */
    private void sendNotification(String id, ListenerType recipient, Notifier<ListenerType> notifier) {
        notifier.sendUpdate(recipient);
    }

    /**
     * Notifies all players in the game
     *
     * @param notifier with players
     */
    public void notifyBroadcast(Notifier<ListenerType> notifier) {
        for (Map.Entry<String, ListenerType> entry : idToListener.entrySet()) {
            sendNotification(entry.getKey(), entry.getValue(), notifier);
        }
    }

    /**
     * Clear all players from the <code>Listener Handler</code>
     */
    public void clear() {
        List<String> users = new ArrayList<>(idToListener.keySet());

        for (String user : users) {
            removeUser(user);
        }
    }
}
