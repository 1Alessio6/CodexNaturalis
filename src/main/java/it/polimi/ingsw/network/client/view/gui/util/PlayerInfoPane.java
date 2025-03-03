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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.client.view.gui.util.GUICards.initializePlayerCards;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

/**
 * PlayerInfoPane represents the pane in which player's cards, rank, resources and switchPlayground resides
 */
public class PlayerInfoPane {

    private final Pane playerMainPane;

    private static final int mainPaneWidth = 361;

    private static final int mainPaneHeight = 162;

    private final Pane playerCardsPane;

    private final Circle status;

    private static final int cardsPaneWidth = 275;

    private static final int cardsPaneHeight = 48;

    private final ResourcePane resourcesPane;

    private final RankPane rank;

    private static final int resourcesPaneWidth = 347;

    private static final int resourcesPaneHeight = 37;

    private final Label username;

    private final ImageView switchPlayground;

    private static final int switchPlaygroundWidth = 30;

    private static final int switchPlaygroundHeight = 30;

    private static final int distance = 25;

    private static final int cardWidth = 74;

    private static final int cardHeight = 48;

    /**
     * Constructs a new <code>PlayerInfoPane</code> with the <code>player</code> provided
     *
     * @param player the representation of the player
     */
    public PlayerInfoPane(ClientPlayer player) {
        playerMainPane = new Pane();
        //playerMainPane.setBackground(setBackgroundColor(convertPlayerColorIntoHexCode(player.getColor())));
        playerMainPane.setPrefSize(mainPaneWidth, mainPaneHeight);
        playerMainPane.setStyle("-fx-background-radius: 10px;" + "-fx-background-color: #EEE5BC;" + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0.5, 0, 0);");

        rank = new RankPane(55, 40, 30);
        Pane rankMainPane = rank.getMainPane();
        rankMainPane.setBackground(setBackgroundColor("#EEE5BC"));
        rankMainPane.setLayoutX(275);
        rankMainPane.setLayoutY(10);

        playerMainPane.getChildren().add(rankMainPane);

        status = new Circle(6);
        status.setCenterX(260);
        status.setCenterY(25);
        updateStatus(player.isConnected());
        playerMainPane.getChildren().add(status);


        //initialize card pane
        playerCardsPane = new Pane();
        playerCardsPane.setPrefSize(cardsPaneWidth, cardsPaneHeight);

        List<ClientCard> cards = player.getPlayerCards();


        initializePlayerCards(playerCardsPane, cards, cardWidth, cardHeight, distance, MouseButton.PRIMARY);
        playerCardsPane.setLayoutX(15);
        playerCardsPane.setLayoutY(99);
        playerMainPane.getChildren().add(playerCardsPane);

        //initialize player text
        username = new Label(player.getUsername());
        username.setStyle("-fx-text-fill: " + convertPlayerColorIntoHexCode(player.getColor()) + ";" + "-fx-font-weight: bold;" + "-fx-background-radius: 5px;" + "-fx-background-color: #FFFFFF;");
        username.setFont(loadFontLiberationSerifRegular(12.5));
        username.setPrefHeight(15);
        username.setPadding(new Insets(7));
        username.setLayoutX(7);
        username.setLayoutY(15);
        playerMainPane.getChildren().add(username);


        resourcesPane = new ResourcePane(resourcesPaneWidth, resourcesPaneHeight);
        resourcesPane.getResourcesPane().setStyle("-fx-background-radius: 10px;" + "-fx-background-color: #FFFFFF;");
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

    /**
     * Updates the amount of resources for each resource in <code>playgroundResources</code>
     *
     * @param playgroundResources a map containing the updated resources
     */
    public void updateResources(Map<Symbol, Integer> playgroundResources) {
        resourcesPane.updateResources(playgroundResources);
    }

    /**
     * Updates the cards of the player
     *
     * @param cards the players' cards to update
     */
    public void updatePlayerCards(List<ClientCard> cards) {
        playerCardsPane.getChildren().clear();
        initializePlayerCards(playerCardsPane, cards, cardWidth, cardHeight, distance, MouseButton.PRIMARY);
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

    /**
     * Updates the status of the player according to the <code>isConnected</code> provided
     *
     * @param isConnected boolean representing whether the player is connected or not
     */
    public void updateStatus(boolean isConnected){
        if(isConnected){
            status.setStyle("-fx-fill: #32CD32;");
        }
        else{
            status.setStyle("-fx-fill: #A9A9A9;");
        }
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
