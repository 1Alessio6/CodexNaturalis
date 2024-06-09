package it.polimi.ingsw.network.client.view.gui.util;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class GUIUtil {

    public static boolean isClicked(MouseEvent mouseEvent, MouseButton mouseButton) {
        return mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == mouseButton;
    }

}
