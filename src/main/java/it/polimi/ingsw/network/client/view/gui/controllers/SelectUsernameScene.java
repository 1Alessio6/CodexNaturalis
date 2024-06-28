package it.polimi.ingsw.network.client.view.gui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

/**
 * SelectUsernameScene is the controller concerning selectUsername scene
 */
public class SelectUsernameScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private TextField usernameCatcher;

    @FXML
    private Text selectUsernameText;

    @FXML
    private Text maximumLimitText;

    private boolean reachedMaximumCharacters;

    public SelectUsernameScene(){}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        mainPane.setBackground(createMainBackground());
        reachedMaximumCharacters = false;
        selectUsernameText.setFont(loadFontLiberationSerifRegular(24.5));
        maximumLimitText.setFont(loadFontLiberationSerifRegular(15.5));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeUsingGameInformation() {
        super.initializeUsingGameInformation();
        addButtonPane(mainPane, buttonPane, 860, 650);
        usernameCatcher.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    if(!reachedMaximumCharacters) {
                        System.out.println("Trying to connect");
                        gui.getController().connect(usernameCatcher.getText());
                        usernameCatcher.setText("");
                    }
                    else{
                        showError("Your username is too long\nyou exceeded the maximum characters");
                    }
                }

            }
        });

        usernameCatcher.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(usernameCatcher.getText().length() > 12){
                    usernameCatcher.setStyle("-fx-text-fill: #FF0000;");
                    reachedMaximumCharacters = true;
                }

                if(reachedMaximumCharacters && usernameCatcher.getText().length() <= 12){
                    usernameCatcher.setStyle("-fx-text-fill: #000000;");
                    reachedMaximumCharacters = false;
                }
            }
        });
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
        errorPane.setLayoutX((getSceneWindowWidth() - errorPaneWidth)/2);
        errorPane.setLayoutY(10);
        mainPane.getChildren().add(errorPane);
    }

    /**
     * {@inheritDoc}
     */
    public double getSceneWindowWidth() {
        return connectionSceneWidth;
    }

    /**
     * {@inheritDoc}
     */
    public double getSceneWindowHeight() {
        return connectionSceneHeight;
    }

}
