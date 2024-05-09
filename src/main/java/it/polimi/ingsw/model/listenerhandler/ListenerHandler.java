package it.polimi.ingsw.model.listenerhandler;

import it.polimi.ingsw.model.notifier.Notifier;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerHandler <ListenerType> {
    Map<String, ListenerType> idToListener;

    public ListenerHandler() {
        idToListener = new HashMap<>();
    }

    public ListenerHandler(Map<String, ListenerType> idToListener) {
        this.idToListener = new HashMap<>(idToListener);
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
        try {
            notifier.sendUpdate(idToListener.get(username));
        } catch (RemoteException remoteException) {
            System.out.println("Disconnected listener");
            idToListener.remove(username);
        }
    }

    public void notifyBroadcast(Notifier<ListenerType> notifier) {
        List<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, ListenerType> entry : idToListener.entrySet()) {
            try {
                notifier.sendUpdate(entry.getValue());
            } catch (RemoteException e) {
                toRemove.add(entry.getKey());
            }
        }

        for (String username : toRemove) {
            System.out.println("Disconnected listener");
            idToListener.remove(username);
        }
    }

    public void clear() {
        idToListener.clear();
    }
}
