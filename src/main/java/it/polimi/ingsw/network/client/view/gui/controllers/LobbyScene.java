package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

public class LobbyScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private TextArea connectedPlayers;

    @FXML
    private Text setNumberRequest;

    @FXML
    private ComboBox<Integer> numberPlayerCatcher;

    private Text requiredPlayer;

    @FXML
    private Text connectedPlayerText;


    public LobbyScene() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        requiredPlayer = new Text();
        requiredPlayer.setLayoutX(580.0);
        requiredPlayer.setLayoutY(520.0);
        mainPane.getChildren().add(requiredPlayer);
        numberPlayerCatcher.getItems().addAll(2, 3, 4);
        Text title = new Text("Lobby");
        title.setFont(loadTitleFont(50));
        title.setLayoutY(94);
        title.setLayoutX(600);
        title.setStrokeType(StrokeType.OUTSIDE);
        mainPane.getChildren().add(title);
        numberPlayerCatcher.setVisible(false);
        setNumberRequest.setVisible(false);
        mainPane.setBackground(createMainBackground());
        connectedPlayers.setFont(loadFontLiberationSerifBold(15));
        connectedPlayerText.setFont(loadFontLiberationSerifRegular(15));
        setNumberRequest.setFont(loadFontLiberationSerifRegular(15));
        requiredPlayer.setFont(loadFontLiberationSerifRegular(15));
    }

    /**
     * {@inheritDoc}
     */
    public void initializeUsingGameInformation() {
        super.initializeUsingGameInformation();
        addButtonPane(mainPane, buttonPane, 1028, 637);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeUpdatePaneFromMainPane(StackPane errorPane) {
        mainPane.getChildren().remove(errorPane);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String details) {
        StackPane errorPane = generateError(details);
        errorPane.setLayoutX((getSceneWindowWidth() - errorPaneWidth) / 2);
        errorPane.setLayoutY(10);
        mainPane.getChildren().add(errorPane);
    }


    /**
     * Initializes the creator scene, makes <code>numberPlayerCatcher</code> and <code>setNumberRequest</code> visible.
     */
    public void initializeCreatorScene() {
        numberPlayerCatcher.setVisible(true);
        setNumberRequest.setVisible(true);
    }

    /**
     * Write the username of the player connected into the main text area.
     *
     * @param usernames the usernames of the connected players.
     */
    public void setPlayerConnected(List<String> usernames) {
        connectedPlayers.clear();
        for (int i = 0; i < usernames.size(); i++) {
            int playerNumber = i + 1;
            connectedPlayers.appendText(playerNumber + " - " + usernames.get(i));

            if (usernames.get(i).equals(gui.getController().getMainPlayerUsername())) {
                connectedPlayers.appendText(" (You)");
            }

            connectedPlayers.appendText("\n");
        }
    }

    @FXML
    private void setPlayersNumber() {

        try {
            gui.getController().setPlayersNumber(numberPlayerCatcher.getValue());
        } catch (InvalidPlayersNumberException ignored) {
        }

        numberPlayerCatcher.setVisible(false);
        setNumberRequest.setVisible(false);
        int numberRequiredPlayer = numberPlayerCatcher.getValue();
        requiredPlayer.setText("Player required to play: " + numberRequiredPlayer + " ");
        requiredPlayer.setVisible(true);

    }

    /**
     * {@inheritDoc}
     */
    public double getSceneWindowWidth() {
        return startedGameSceneWidth;
    }

    /**
     * {@inheritDoc}
     */
    public double getSceneWindowHeight() {
        return startedGameSceneHeight;
    }


}
