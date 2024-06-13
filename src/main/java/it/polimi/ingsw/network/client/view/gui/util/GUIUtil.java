package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class GUIUtil {

    public static final String  Font = "Cambria Math";

    public static boolean isClicked(MouseEvent mouseEvent, MouseButton mouseButton) {
        return mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == mouseButton;
    }

    public static Color convertPlayerColor(PlayerColor color) {
        switch (color) {
            case RED -> {
                return Color.RED;
            }
            case BLACK -> {
                return Color.BLACK;
            }
            case BLUE -> {
                return Color.BLUE;
            }
            case GREEN -> {
                return Color.GREEN;
            }
            case YELLOW -> {
                return Color.YELLOW;
            }
        }
        return null;
    }

    public static String convertPlayerColorIntoHexCode(PlayerColor color) {
        switch (color) {
            case RED -> {
                return "#FF0000";
            }
            case BLACK -> {
                return "#000000";
            }
            case BLUE -> {
                return "#0000FF";
            }
            case GREEN -> {
                return "#00FF00";
            }
            case YELLOW -> {
                return "#FCBA03"; //other possible solution #DAA520, #FFD700
            }
        }
        return null;
    }

    public static Background setBackgroundColor(String backgroundColor) {
        return new Background(new BackgroundFill(Color.web(backgroundColor), CornerRadii.EMPTY, null));
    }

    public static Icon convertRankIntoIcon(int rank){
        return switch (rank) {
            case 1 -> Icon.FIRST;
            case 2 -> Icon.SECOND;
            case 3 -> Icon.THIRD;
            case 4 -> Icon.FOURTH;
            default -> null;
        };
    }



}
