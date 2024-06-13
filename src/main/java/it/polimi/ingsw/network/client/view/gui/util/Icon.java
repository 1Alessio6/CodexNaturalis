package it.polimi.ingsw.network.client.view.gui.util;

public enum Icon {

    //todo add credits icons8 and flatIcons for 4th place

    OBSERVE_PLAYGROUND("/gui/png/icons/observe_playground.png"),

    HOME("/gui/png/icons/home.png"),

    SETTINGS("/gui/icons/.png"),

    FIRST("/gui/png/icons/first-place.png"),

    SECOND("/gui/png/icons/second-place.png"),

    THIRD("/gui/png/icons/third-place.png"),

    FOURTH("/gui/png/icons/fourth-place.png");



    private final String path;

    Icon(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }

}
