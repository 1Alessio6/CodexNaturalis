package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

public class PlayerInfoPane {

    private Pane playerMainPane;

    private static final int mainPaneWidth = 361;

    private static final int mainPaneHeight = 162;

    private Pane playerCardsPane;

    private static final int cardsPaneWidth = 275;

    private static final int cardsPaneHeight = 48;

    private final ResourcePane resourcesPane;

    private final RankPane rank;

    private static final int resourcesPaneWidth = 347;

    private static final int resourcesPaneHeight = 37;

    private final Label username;

    private ImageView switchPlayground;

    private static final int switchPlaygroundWidth = 30;

    private static final int switchPlaygroundHeight = 30;

    private static final int distance = 25;

    private static final int cardWidth = 74;

    private static final int cardHeight = 48;


    public PlayerInfoPane(ClientPlayer player) {
        playerMainPane = new Pane();
        playerMainPane.setBackground(setBackgroundColor("#EEE5BC"));
        //playerMainPane.setBackground(setBackgroundColor(convertPlayerColorIntoHexCode(player.getColor())));
        playerMainPane.setPrefSize(mainPaneWidth, mainPaneHeight);

        rank = new RankPane(55,40,30);
        Pane rankMainPane = rank.getMainPane();
        rankMainPane.setBackground(setBackgroundColor("#EEE5BC"));
        rankMainPane.setLayoutX(275);
        rankMainPane.setLayoutY(10);

        playerMainPane.getChildren().add(rankMainPane);


        //initialize card pane
        playerCardsPane = new Pane();
        playerCardsPane.setPrefSize(cardsPaneWidth, cardsPaneHeight);

        //todo remove this line and pass player cards

        List<ClientCard> cards = player.getPlayerCards();


        GUICards.initializePlayerCards(playerCardsPane, cards, cardWidth, cardHeight, distance, MouseButton.PRIMARY);
        playerCardsPane.setLayoutX(15);
        playerCardsPane.setLayoutY(99);
        playerMainPane.getChildren().add(playerCardsPane);

        //initialize player text
        username = new Label(player.getUsername());
        username.setStyle("-fx-text-fill: " + convertPlayerColorIntoHexCode(player.getColor()) + ";");
        username.setFont(new Font("Cambria Math", 12));
        username.setPrefHeight(15);
        username.setBackground(GUIUtil.setBackgroundColor("#FFFFFF"));
        username.setPadding(new Insets(7));
        username.setLayoutX(7);
        username.setLayoutY(15);
        playerMainPane.getChildren().add(username);


        resourcesPane = new ResourcePane(resourcesPaneWidth, resourcesPaneHeight);
        resourcesPane.setBackground("#FFFFFF");
        resourcesPane.initialize(30.45, 33.6, 19.5);
        updateResources(player.getPlayground().getResources());
        resourcesPane.getResourcesPane().setLayoutX(7);
        resourcesPane.getResourcesPane().setLayoutY(51);
        playerMainPane.getChildren().add(resourcesPane.getResourcesPane());


        switchPlayground = new ImageView(Icon.OBSERVE_PLAYGROUND.getPath());
        switchPlayground.setLayoutX(310);
        switchPlayground.setLayoutY(108);
        switchPlayground.setFitWidth(switchPlaygroundWidth);
        switchPlayground.setFitHeight(switchPlaygroundHeight);
        playerMainPane.getChildren().add(switchPlayground);

    }

    public void updateResources(Map<Symbol, Integer> playgroundResources){
        resourcesPane.updateResources(playgroundResources);
    }

    public void updatePlayerCards(List<ClientCard> cards) {
        playerCardsPane.getChildren().clear();
        GUICards.initializePlayerCards(playerCardsPane, cards, cardWidth, cardHeight, distance, MouseButton.PRIMARY);
    }

    public void updateRank(int rank){
        this.rank.updateRank(rank);
    }

    public void updateScore(int score){
        this.rank.updateScore(score);
    }

    public ImageView getSwitchPlayground() {
        return switchPlayground;
    }

    public Pane getPlayerMainPane() {
        return playerMainPane;
    }

    public String getPlayerUsername() {
        return username.getText();
    }
}
