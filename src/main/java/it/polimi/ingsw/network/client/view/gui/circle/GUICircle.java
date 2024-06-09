package it.polimi.ingsw.network.client.view.gui.circle;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import javafx.scene.shape.Circle;

public class GUICircle {
    private Circle circle;
    private PlayerColor color;

    public GUICircle(double x, double y, double radius) {
        this.circle = new Circle(x, y, radius);
        this.circle.setVisible(false);
    }

    public PlayerColor getColor() {
        return color;
    }
}
