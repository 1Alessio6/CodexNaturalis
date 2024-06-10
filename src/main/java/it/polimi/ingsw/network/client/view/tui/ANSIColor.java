package it.polimi.ingsw.network.client.view.tui;

/**
 * Enumeration representing some Ansi escape codes for stylizing the output.
 */
public enum ANSIColor {
    /**
     * RESET turns off all properties
     */
    RESET("\033[0m"),
    /**
     * STRIKETHROUGH strikes out the characters
     */
    STRIKETHROUGH("\033[9m"),

    BLACK("\033[0;30m"),
    /**
     * RED changes the color of the text to red
     */
    RED("\033[0;31m"),
    /**
     * GREEN changes the color of the text to green
     */
    GREEN("\033[0;32m"),
    /**
     * YELLOW changes the color of the text to yellow
     */
    YELLOW("\033[33m"),
    /**
     * BLUE changes the color of the text to blue
     */
    BLUE("\033[0;34m"),
    /**
     * PURPLE changes the color of the text to purple
     */
    PURPLE("\033[0;35m"),
    BLACK_BOLD("\033[1m"),
    /**
     * ITALIC makes the text italicised
     */
    ITALIC("\033[3m"),
    /**
     * PURPLE_BACKGROUND_HIGH_INTENSITY sets the background color to purple
     */
    PURPLE_BACKGROUND_HIGH_INTENSITY("\033[0;105m"),
    /**
     * RED_BACKGROUND_BRIGHT sets the background color to red
     */
    RED_BACKGROUND_BRIGHT("\033[0;101m"),
    /**
     * BLUE_BACKGROUND_BRIGHT sets the background color to blue
     */
    BLUE_BACKGROUND_BRIGHT("\033[0;104m"),
    /**
     * YELLOW_BACKGROUND_BRIGHT sets the background color to yellow
     */
    YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),
    /**
     * BLACK_BOLD_BRIGHT sets the background color to bright bold black
     */
    BLACK_BOLD_BRIGHT("\033[1;90m"),
    /**
     * GREEN_BACKGROUND_BRIGHT sets the background color to green
     */
    GREEN_BACKGROUND_BRIGHT("\033[0;102m");

    private final String color;

    public String getColor() {
        return color;
    }

    ANSIColor(String color) {
        this.color = color;
    }
}
