package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.network.client.view.gui.ApplicationGUI;
import it.polimi.ingsw.network.client.view.gui.util.Icon;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.Stack;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.convertPlayerColorIntoHexCode;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.initializeIconImageView;

/**
 * SceneController is an interface for scene controllers
 */
public abstract class SceneController {
    protected ApplicationGUI gui;

    protected Pane buttonPane;

    private boolean rulebookOpened;

    private boolean settingsOpened;

    private static final double rulebookPageWidth = 561.6;

    private static final double rulebookPageHeight = 562.8;


    public void setGui(ApplicationGUI gui) {
        this.gui = gui;
    }

    /**
     * Method used to initialize the scene
     */
    public void initialize() {

    }

    private Button initializeSettings() {

        Button settings = new Button();
        settings.setPrefSize(50, 50);
        settings.setGraphic(initializeIconImageView(Icon.SETTINGS.getPath(), 30));

        Button fullscreenButton = initializeFullScreenButton();
        Pane mainPane = new Pane();
        mainPane.setPrefSize(500, 500);
        mainPane.getChildren().add(fullscreenButton);
        Scene settingsScene = new Scene(mainPane);
        Stage settingsStage = new Stage();
        initializePopUpScene(settingsStage, settingsScene, Icon.SETTINGS);

        settings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!settingsOpened) {
                    settingsStage.show();
                    settingsOpened = true;
                } else {
                    settingsStage.close();
                    settingsOpened = false;
                }
            }
        });

        settings.setLayoutY(17.5);
        settings.setLayoutX(72);

        return settings;
    }

    private Button initializeRulebook() {

        Button rulebook = new Button();
        rulebookOpened = false;
        rulebook.setPrefSize(50, 50);
        rulebook.setGraphic(initializeIconImageView(Icon.RULEBOOK.getPath(), 30));

        Pagination rulebookPagination = initializeRulebookPagination();
        Scene rulebookScene = new Scene(rulebookPagination, rulebookPageWidth, rulebookPageHeight);
        Stage rulebookStage = new Stage();
        initializePopUpScene(rulebookStage, rulebookScene, Icon.RULEBOOK);

        rulebook.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!rulebookOpened) {
                    rulebookStage.show();
                    rulebookOpened = true;
                } else {
                    rulebookStage.close();
                    rulebookOpened = false;
                }
            }
        });

        rulebook.setLayoutY(17.5);
        rulebook.setLayoutX(152);

        return rulebook;
    }

    private void initializePopUpScene(Stage popUpStage, Scene popUpScene, Icon typeOfPopUp) {

        assert (typeOfPopUp == Icon.SETTINGS || typeOfPopUp == Icon.RULEBOOK);

        popUpStage.initOwner(gui.getPrimaryStage());
        popUpStage.setScene(popUpScene);
        popUpStage.setResizable(false);

        if (typeOfPopUp == Icon.SETTINGS) {
            popUpStage.setTitle("Settings");
        } else {
            popUpStage.setTitle("Rulebook");
        }

        popUpStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                if (typeOfPopUp == Icon.SETTINGS) {
                    settingsOpened = false;
                } else {
                    rulebookOpened = false;
                }
                popUpStage.close();
            }
        });
    }

    private Pagination initializeRulebookPagination() {
        Pagination rulebookPagination = new Pagination(12);
        rulebookPagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                pageIndex++;
                return createRulebookPage(pageIndex);
            }
        });

        return rulebookPagination;
    }

    private ImageView createRulebookPage(int pageIndex) {
        assert (pageIndex >= 0 && pageIndex < 12);
        ImageView page = new ImageView("/gui/png/rulebook/CODEX_Rulebook_IT-" + pageIndex + ".png");
        page.setFitWidth(rulebookPageWidth);
        page.setFitHeight(rulebookPageHeight);

        return page;
    }

    private Button initializeFullScreenButton() {
        Button fullscreenButton = new Button("FullScreen");

        fullscreenButton.setPrefSize(70, 20);
        fullscreenButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (gui.getIsFullScreen()) {
                    gui.setWindowScreenMode();
                } else {
                    gui.setFullScreenMode();
                }
            }
        });

        return fullscreenButton;
    }

    public abstract double getSceneWindowWidth();

    public abstract double getSceneWindowHeight();

    /**
     * Method used to initialize game information
     */
    public void initializeUsingGameInformation() {
        buttonPane = new Pane();
        buttonPane.setPrefSize(202, 85);
        buttonPane.getChildren().add(initializeSettings());
        buttonPane.getChildren().add(initializeRulebook());
    }




    private void fadeOutError(StackPane errorPane){
        FadeTransition transition = new FadeTransition(Duration.seconds(1),errorPane);
        transition.setFromValue(errorPane.getOpacity());
        transition.setToValue(0);
        transition.setOnFinished(actionEvent -> removeErrorFromMainPane(errorPane));
        transition.play();
    }

    protected abstract void removeErrorFromMainPane(StackPane errorPane);

    public void showError(String details) {

    }

}
