package it.polimi.ingsw.network.client.view.gui.util;

public enum Icon {

    OBSERVE_PLAYGROUND("/gui/png/icons/observe_playground.png"),

    HOME("/gui/png/icons/home.png"),

    SETTINGS("/gui/icons/.png");



    private final String path;

    Icon(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }

}
