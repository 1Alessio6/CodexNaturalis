package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * GUIUtil is the class that facilitates GUIs realization
 */
public class GUIUtil {

    public static final String CAMBRIA_MATH = "Cambria Math";

    public static final double startedGameSceneHeight = 755.0;

    public static final double startedGameSceneWidth = 1325.0;

    public static final double connectionSceneHeight = 720.0;

    public static final double connectionSceneWidth = 1080.0;

    /**
     * Checks whether the <code>mouseButton</code> has been clicked or not
     *
     * @param mouseEvent  the event that has taken place on the mouse
     * @param mouseButton the mouse button in which check
     * @return true if the mouse has been clicked, false otherwise
     */
    public static boolean isClicked(MouseEvent mouseEvent, MouseButton mouseButton) {
        return mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == mouseButton;
    }

    public static Font loadTitleFont(double size){
        return Font.loadFont(GUIUtil.class.getResourceAsStream("/gui/fonts/FetteKanzlei.ttf"), size);
    }

    public static ImageView initializeIconImageView(String path, double fitSize){
        ImageView image = new ImageView(path);
        image.setFitWidth(fitSize);
        image.setFitHeight(fitSize);

        return image;
    }

    /**
     * Converts the <code>PlayerColor color</code> into <code>Color</code>
     *
     * @param color the <code>PlayerColor</code> to be converted
     * @return the <code>Color color</code> corresponding to the <code>PlayerColor color</code>
     */
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

    /**
     * Converts the <code>PlayerColor color</code> into hexadecimal code
     *
     * @param color the <code>PlayerColor</code> to be converted
     * @return the hexadecimal code corresponding to the <code>PlayerColo color</code>
     */
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

    /**
     * Provides a Background with the <code>backgroundColor</code> provided
     *
     * @param backgroundColor the color of the background
     * @return the Background with the <code>backgroundColor</code> indicated
     */
    public static Background setBackgroundColor(String backgroundColor) {
        return new Background(new BackgroundFill(Color.web(backgroundColor), CornerRadii.EMPTY, null));
    }

    /**
     * Converts the <code>rank</code> into <code>Icon</code>
     *
     * @param rank the integer corresponding player's rank
     * @return the <code>Icon</code> correlated to the <code>rank</code>
     */
    public static Icon convertRankIntoIcon(int rank){
        return switch (rank) {
            case 1 -> Icon.FIRST;
            case 2 -> Icon.SECOND;
            case 3 -> Icon.THIRD;
            case 4 -> Icon.FOURTH;
            default -> null;
        };
    }

    /**
     * Creates the main background
     *
     * @return the <code>Background</code> created
     */
    public static Background createMainBackground(){
        BackgroundImage backgroundImage = new BackgroundImage(new Image("/gui/png/background_tile.png"), BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        return new Background(backgroundImage);
    }

    public static void addButtonPane(Pane mainPane, Pane buttonPane, double layoutX, double layoutY){
        buttonPane.setLayoutX(layoutX);
        buttonPane.setLayoutY(layoutY);
        mainPane.getChildren().add(buttonPane);
    }




}
