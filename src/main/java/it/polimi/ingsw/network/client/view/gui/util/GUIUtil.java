package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class GUIUtil {

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

    public static Background setBackgroundColor(String backgroundColor) {
        return new Background(new BackgroundFill(Color.web(backgroundColor), CornerRadii.EMPTY, null));
    }



}
