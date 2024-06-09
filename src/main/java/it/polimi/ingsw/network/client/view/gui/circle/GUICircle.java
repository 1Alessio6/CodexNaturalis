package it.polimi.ingsw.network.client.view.gui.circle;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import javafx.scene.shape.Circle;

public class GUICircle {
    private Circle circle;
    private PlayerColor color;

    public GUICircle(double x, double y, double radius, PlayerColor color) {
        this.circle = new Circle(x, y, radius);
        this.circle.setVisible(false);
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
