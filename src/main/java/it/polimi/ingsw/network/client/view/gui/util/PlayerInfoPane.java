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

    private final Pane resourcesPane;

    private static final int resourcesPaneWidth = 347;

    private static final int resourcesPaneHeight = 37;

    private final Text username;

    private ImageView switchPlayground;

    private static final int switchPlaygroundWidth = 30;

    private static final int switchPlaygroundHeight = 30;

    private static final int distance = 25;

    private static final int cardWidth = 74;

    private static final int cardHeight = 48;

    private final Map<Symbol, Text> resources;


    public PlayerInfoPane(ClientPlayer player) {
        resources = new HashMap<>();
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


        resourcesPane = new Pane();
        resourcesPane.setPrefSize(resourcesPaneWidth, resourcesPaneHeight);
        resourcesPane.setBackground(setBackgroundColor("#FFFFFF"));
        initializeResources();
        updateResources(player.getPlayground().getResources());
        resourcesPane.setLayoutX(7);
        resourcesPane.setLayoutY(51);
        playerMainPane.getChildren().add(resourcesPane);


        switchPlayground = new ImageView(Icon.OBSERVE_PLAYGROUND.getPath());
        switchPlayground.setLayoutX(310);
        switchPlayground.setLayoutY(108);
        switchPlayground.setFitWidth(switchPlaygroundWidth);
        switchPlayground.setFitHeight(switchPlaygroundHeight);
        playerMainPane.getChildren().add(switchPlayground);


    }

    public void setPlayerCardsPane(Pane playerCardsPane) {
        this.playerCardsPane = playerCardsPane;
    }

    private void initializeResources() {

        double layoutX = 2.0;

        for (Symbol symbol : Symbol.values()) {
            ImageView symbolImage = new ImageView(symbol.getPath());
            symbolImage.setFitHeight(33.6);
            symbolImage.setFitWidth(30.45);
            symbolImage.setLayoutX(layoutX);
            Text points = new Text();
            resources.put(symbol, points);
            points.setLayoutX(layoutX + 30.45 + 5);
            points.setLayoutY(22);
            resourcesPane.getChildren().add(symbolImage);
            resourcesPane.getChildren().add(points);
            layoutX = layoutX + 50.0;
        }
    }

    public void updateResources(Map<Symbol, Integer> playgroundResources){
        for(Symbol symbol : playgroundResources.keySet()){
            resources.get(symbol).setText(playgroundResources.get(symbol).toString());
        }
    }

    public void updatePlayerCards(List<ClientCard> cards) {
        GUICards.initializePlayerCards(playerCardsPane, cards, cardWidth, cardHeight, distance, MouseButton.PRIMARY);
    }

    public Pane getPlayerMainPane() {
        return playerMainPane;
    }

    public String getPlayerUsername() {
        return username.getText();
    }
}
