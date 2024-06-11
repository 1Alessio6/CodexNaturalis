package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.client.view.gui.util.GUIUtil;
import javafx.scene.shape.Circle;

public class GUICircle {
    private Circle circle;
    private PlayerColor color;

    public GUICircle(double radius, PlayerColor color) {
        this.circle = new Circle(radius);
        this.circle.setVisible(false);
        this.circle.setFill(GUIUtil.convertPlayerColor(color));
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setVisibility(boolean visibility) {
        circle.setVisible(visibility);
    }

    public void setCoordinates(double x, double y) {
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

}
