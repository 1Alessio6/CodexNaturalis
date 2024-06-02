package it.polimi.ingsw.network.client.view.tui;

public enum ANSIColor {
    RESET("\033[0m"),
    STRIKETHROUGH("\033[9m"),

    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[33m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[0;35m"),
    BLACK_BOLD("\033[1m"),
    ITALIC("\033[3m"),
    PURPLE_BACKGROUND_HIGH_INTENSITY("\033[0;105m"),
    RED_BACKGROUND_BRIGHT("\033[0;101m"),
    BLUE_BACKGROUND_BRIGHT("\033[0;104m"),
    YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),
    BLACK_BOLD_BRIGHT("\033[1;90m"),
    GREEN_BACKGROUND_BRIGHT("\033[0;102m");

    private final String color;

    public String getColor() {
        return color;
    }

    ANSIColor(String color) {
        this.color = color;
    }
}
