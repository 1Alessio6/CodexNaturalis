package it.polimi.ingsw.network.client.view.gui.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.convertRankIntoIcon;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.loadFontLiberationSerifRegular;

/**
 * RankPane represents the pane containing the player's rank and score
 */
public class RankPane {

    private final ImageView rank;

    private final Text score;

    private final Pane mainPane;

    /**
     * Constructs a <code>RankPane</code> with the <code>width</code>, <code>height</code> and <code>iconSize</code>
     * provided
     *
     * @param width    the total width of the <code>RankPane</code> to construct
     * @param height   the total height of the <code>RankPane</code> to construct
     * @param iconSize the size of the icon
     */
    public RankPane(double width, double height, double iconSize){

        mainPane = new Pane();
        mainPane.setPrefSize(width, height);

        rank = new ImageView();
        rank.setFitWidth(iconSize);
        rank.setFitHeight(iconSize);

        score = new Text();
        score.setLayoutY(15);
        score.setLayoutX(iconSize + 5);
        score.setFont(loadFontLiberationSerifRegular(12.5));


        mainPane.getChildren().add(rank);
        mainPane.getChildren().add(score);
    }

    public Pane getMainPane() {
        return mainPane;
    }

    /**
     * Updates the rank of the player
     *
     * @param rank the new rank of the player
     */
    public void updateRank(int rank){
        this.rank.setImage(new Image(convertRankIntoIcon(rank).getPath()));
    }

    /**
     * Updates the score of the player
     *
     * @param score the new score of the player
     */
    public void updateScore(int score){
        this.score.setText("SCORE \n     " + score);
    }
}
