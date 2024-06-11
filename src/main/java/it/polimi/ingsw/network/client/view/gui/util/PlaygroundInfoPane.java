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

    ImageView returnToMainPlayground;
    ResourcePane resourcePane;

    public PlaygroundInfoPane(ClientPlayer player){

        returnToMainPlayground = new ImageView(Icon.HOME.getPath());
        returnToMainPlayground.setFitWidth(30);
        returnToMainPlayground.setFitHeight(30);
        returnToMainPlayground.setLayoutX(600);
        returnToMainPlayground.setLayoutY(5);

        playGroundOwner = new Label();
        mainPane = new Pane();
        mainPane.setPrefSize(643.08, 40);
        mainPane.setLayoutX(345);
        mainPane.setLayoutY(183);
        mainPane.setBackground(setBackgroundColor("#EEE5BC"));

        playGroundOwner.setText(player.getUsername() + "'s Playground");
        playGroundOwner.setFont(new Font("Arial", 11));
        playGroundOwner.setLayoutY(15);
        playGroundOwner.setLayoutX(5);

        resourcePane = new ResourcePane(400, 40);
        resourcePane.initialize(36.25,40,20);
        resourcePane.setBackground("#EEE5BC");
        resourcePane.updateResources(player.getPlayground().getResources());
        Pane resource = resourcePane.getResourcesPane();
        resource.setLayoutX(100);
        mainPane.getChildren().add(resource);
        mainPane.getChildren().add(playGroundOwner);
        mainPane.getChildren().add(returnToMainPlayground);
    }

    public void isMainPlayer(){
        playGroundOwner.setText("Your Playground");
        returnToMainPlayground.setVisible(false);
    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void updateResources(Map<Symbol, Integer> playgroundResources){
        updateResources(playgroundResources);
    }

    public ImageView getReturnToMainPlayground(){
        return returnToMainPlayground;
    }

}
