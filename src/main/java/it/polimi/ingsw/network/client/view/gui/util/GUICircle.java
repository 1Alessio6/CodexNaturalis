package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.color.PlayerColor;
import javafx.scene.shape.Circle;

/**
 * GUICircle creates the circles containing the colors of the player tokens
 */
public class GUICircle {
    private Circle circle;
    private PlayerColor color;

    /**
     * Constructs a <code>GUICircle</code> with the <code>radius</code> and the <code>color</code>
     *
     * @param radius the circle radius
     * @param color  the color of the player's token
     */
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
