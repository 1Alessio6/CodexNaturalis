package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.setBackgroundColor;

public class ResourcePane {

    private final double paneHeight;
    private Map<Symbol, Text> resources;
    private final Pane resourcesPane;

    public ResourcePane(double paneWidth, double paneHeight) {
        resourcesPane = new Pane();
        resourcesPane.setPrefSize(paneWidth, paneHeight);
        resources = new HashMap<>();
        this.paneHeight = paneHeight;
    }

    public void initialize(double resourceWidth, double resourceHeight, double distance) {

        double layoutX = 2.0;

        for (Symbol symbol : Symbol.values()) {
            ImageView symbolImage = new ImageView(symbol.getPath());
            symbolImage.setFitHeight(resourceHeight);
            symbolImage.setFitWidth(resourceWidth);
            symbolImage.setLayoutX(layoutX);
            Text points = new Text();
            resources.put(symbol, points);
            points.setLayoutX(layoutX + resourceWidth + 5);
            points.setLayoutY(paneHeight * 0.595);
            System.err.println(points.getY());
            System.err.println(resourcesPane.getHeight());
            resourcesPane.getChildren().add(symbolImage);
            resourcesPane.getChildren().add(points);
            layoutX = layoutX + resourceWidth + distance;
        }
    }

    public Pane getResourcesPane() {
        return resourcesPane;
    }

    public void setBackground(String color) {
        resourcesPane.setBackground(setBackgroundColor(color));
    }

    public void updateResources(Map<Symbol, Integer> playgroundResources) {
        for (Symbol symbol : playgroundResources.keySet()) {
            resources.get(symbol).setText(playgroundResources.get(symbol).toString());
        }
    }
}
