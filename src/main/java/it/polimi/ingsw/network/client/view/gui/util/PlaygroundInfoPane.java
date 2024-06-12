package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.util.Map;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.setBackgroundColor;
import static javax.swing.text.StyleConstants.setBackground;

public class PlaygroundInfoPane {

    Pane mainPane;
    Label playGroundOwner;

    private final RankPane rank;


    ImageView returnToMainPlayground;
    ResourcePane resourcePane;

    public PlaygroundInfoPane() {
        returnToMainPlayground = new ImageView(Icon.HOME.getPath());
        returnToMainPlayground.setFitWidth(30);
        returnToMainPlayground.setFitHeight(30);
        returnToMainPlayground.setLayoutX(600);
        returnToMainPlayground.setLayoutY(5);

        rank = new RankPane(55,40,30);
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

        playGroundOwner.setLayoutY(15);
        playGroundOwner.setLayoutX(5);

        resourcePane = new ResourcePane(400, 40);
        resourcePane.initialize(36.25, 40, 20);
        resourcePane.setBackground("#EEE5BC");

        Pane resource = resourcePane.getResourcesPane();
        resource.setLayoutX(150);
        mainPane.getChildren().add(resource);
        mainPane.getChildren().add(playGroundOwner);
        mainPane.getChildren().add(returnToMainPlayground);
    }

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
        playGroundOwner.setFont(new Font("Arial", 11));

        resourcePane.updateResources(player.getPlayground().getResources());
    }

    public void updateRank(int rank){
        this.rank.updateRank(rank);
    }

    public void updateScore(int score){
        this.rank.updateScore(score);
    }

    public Pane getMainPane() {
        return mainPane;
    }


    public ImageView getReturnToMainPlayground(){
        return returnToMainPlayground;
    }

}
