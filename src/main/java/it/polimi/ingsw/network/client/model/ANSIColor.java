package it.polimi.ingsw.network.client.model;

public enum ANSIColor {
    RESET("\033[0m"),
    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[33m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[0;35m");

    private final String color;

    public String getColor() {
        return color;
    }

    ANSIColor(String color) {
        this.color = color;
    }
}
