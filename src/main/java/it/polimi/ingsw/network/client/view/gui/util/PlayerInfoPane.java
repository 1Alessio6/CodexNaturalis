package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.convertPlayerColor;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.setBackgroundColor;

public class PlayerInfoPane {

    private Pane playerMainPane;

    private static final int mainPaneWidth = 361;

    private static final int mainPaneHeight = 162;

    private Pane playerCardsPane;

    private static final int cardsPaneWidth = 275;

    private static final int cardsPaneHeight = 48;

    private final ResourcePane resourcesPane;

    private static final int resourcesPaneWidth = 347;

    private static final int resourcesPaneHeight = 37;

    private final Text username;

    private ImageView switchPlayground;

    private static final int switchPlaygroundWidth = 30;

    private static final int switchPlaygroundHeight = 30;

    private static final int distance = 25;

    private static final int cardWidth = 74;

    private static final int cardHeight = 48;


    public PlayerInfoPane(ClientPlayer player) {
        playerMainPane = new Pane();
        playerMainPane.setBackground(setBackgroundColor("#EEE5BC"));
        playerMainPane.setPrefSize(mainPaneWidth, mainPaneHeight);

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
        username = new Text(player.getUsername());
        username.setFill(convertPlayerColor(player.getColor()));
        username.setLayoutX(15);
        username.setLayoutY(26);
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
