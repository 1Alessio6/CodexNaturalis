package it.polimi.ingsw.network.client.view.gui.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.convertRankIntoIcon;

public class RankPane {

    private final ImageView rank;

    private final Text score;

    private final Pane mainPane;


    public RankPane(double width, double height, double iconSize){

        mainPane = new Pane();
        mainPane.setPrefSize(width, height);

        rank = new ImageView();
        rank.setFitWidth(iconSize);
        rank.setFitHeight(iconSize);

        score = new Text();
        score.setLayoutY(15);
        score.setLayoutX(iconSize + 5);
        score.setFont(new Font("Cambria Math", 12));


        mainPane.getChildren().add(rank);
        mainPane.getChildren().add(score);
    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void updateRank(int rank){
        this.rank.setImage(new Image(convertRankIntoIcon(rank).getPath()));
    }

    public void updateScore(int score){
        this.score.setText("SCORE \n     " + score);
    }
}
