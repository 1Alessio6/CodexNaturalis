package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.util.Map;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;
import static javax.swing.text.StyleConstants.setBackground;

/**
 * PlaygroundInfoPane represents the pane in which player's playground resides.
 * PlaygroundInfoPane includes a <code>ResourcePane</code> and a <code>RankPane</code>
 */
public class PlaygroundInfoPane {

    private final Pane mainPane;

    private final Label playGroundOwner;

    private final RankPane rank;

    private final ImageView returnToMainPlayground;

    private final ResourcePane resourcePane;

    /**
     * Constructs a new <code>PlaygroundInfoPane</code>
     */
    public PlaygroundInfoPane() {
        returnToMainPlayground = new ImageView(Icon.HOME.getPath());
        returnToMainPlayground.setFitWidth(30);
        returnToMainPlayground.setFitHeight(30);
        returnToMainPlayground.setLayoutX(600);
        returnToMainPlayground.setLayoutY(5);

        rank = new RankPane(55, 40, 30);
        Pane rankMainPane = rank.getMainPane();
        rankMainPane.setLayoutX(560);
        rankMainPane.setLayoutY(5);


        playGroundOwner = new Label();
        mainPane = new Pane();
        mainPane.setPrefSize(643.08, 40);
        mainPane.setLayoutX(345);
        mainPane.setLayoutY(183);
        mainPane.setBackground(setBackgroundColor("#EEE5BC"));
        mainPane.getChildren().add(rankMainPane);
        mainPane.setStyle("-fx-background-radius: 5px;" + "-fx-background-color: #EEE5BC;");


        playGroundOwner.setLayoutY(9);
        playGroundOwner.setLayoutX(5);
        playGroundOwner.setFont(loadFontLiberationSerifRegular(12));
        playGroundOwner.setPrefWidth(143);
        playGroundOwner.setWrapText(true);


        resourcePane = new ResourcePane(400, 40);
        resourcePane.initialize(36.25, 40, 20);
        resourcePane.setBackground("#EEE5BC");

        Pane resource = resourcePane.getResourcesPane();
        resource.setLayoutX(150);
        mainPane.getChildren().add(resource);
        mainPane.getChildren().add(playGroundOwner);
        mainPane.getChildren().add(returnToMainPlayground);
    }

    /**
     * Adds playground information
     *
     * @param player       the representation of the player
     * @param isMainPlayer boolean representing whether the <code>player</code> is the main player or not
     */
    public void setPlaygroundInfo(ClientPlayer player, boolean isMainPlayer) {


        if (!isMainPlayer) {
            playGroundOwner.setText(player.getUsername() + "'s Playground");
            rank.getMainPane().setVisible(false);
            returnToMainPlayground.setVisible(true);
        } else {
            playGroundOwner.setText("Your Playground");
            rank.getMainPane().setVisible(true);
            returnToMainPlayground.setVisible(false);
        }
        playGroundOwner.setFont(loadFontLiberationSerifRegular(12));

        resourcePane.updateResources(player.getPlayground().getResources());
    }

    /**
     * Updates the rank of the player
     *
     * @param rank the new rank of the player
     */
    public void updateRank(int rank) {
        this.rank.updateRank(rank);
    }

    /**
     * Updates the score of the player
     *
     * @param score the new score of the player
     */
    public void updateScore(int score) {
        this.rank.updateScore(score);
    }

    public Pane getMainPane() {
        return mainPane;
    }

    /**
     * Provides the home's <code>ImageView</code>
     *
     * @return home's <code>ImageView</code>
     */
    public ImageView getReturnToMainPlayground() {
        return returnToMainPlayground;
    }

}
