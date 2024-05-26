package it.polimi.ingsw.network.client.view.gui;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class GUIPlayground {
    private final int cardWidth;
    private final int cardHeight;
    private static final double xCoefficient = 0.2;
    private static final double yCoefficient = 0.4;

    private double paneWidth;
    private double paneHeight;

    private final double xOffset;
    private final double yOffset;

    private double xUpperStarter;
    private double yUpperStarter;

    public GUIPlayground(int cardWidth, int cardHeight) {
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        this.xOffset = cardWidth - cardWidth * xCoefficient;
        this.yOffset = cardHeight - cardHeight * yCoefficient;
    }

    public void setDimension(ClientPlayground clientPlayground) {
        int[] range = clientPlayground.getRange();
        int xMax = range[0];
        int yMax = range[1];
        paneWidth = 2 * (xOffset * xMax) + cardWidth;
        paneHeight = 2 * (yOffset * yMax) + cardHeight;
        xUpperStarter = xOffset * xMax;
        yUpperStarter = yOffset * yMax;
    }

    public double getPaneWidth() {
        return paneWidth;
    }

    public double getPaneHeight() {
        return paneHeight;
    }

    public Rectangle getRectangle(Position posRelativeToStarter, Image image) {
        double x = xUpperStarter + posRelativeToStarter.getX() * xOffset;
        double y = yUpperStarter - posRelativeToStarter.getY() * yOffset;
        Rectangle rectangle = new Rectangle(x, y, cardWidth, cardHeight);
        rectangle.setFill(new ImagePattern(image));
        return rectangle;
    }
}
