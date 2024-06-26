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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.Optional;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

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
        Pane mainPane = new Pane();
        initializeSettingsSectionTitle("Settings", mainPane, 30, 50);
        initializeSettingsSectionTitle("Commands", mainPane, 30, 350);
        initializeCommandInfoPane(mainPane);

        mainPane.setBackground(createMainBackground());
        mainPane.setPrefSize(485, 1100);
        //mainPane.getChildren().add(fullscreenButton);
        ScrollPane mainScrollPane = new ScrollPane();
        mainScrollPane.setPrefSize(500, 500);
        mainScrollPane.setContent(mainPane);
        Scene settingsScene = new Scene(mainScrollPane);
        Stage settingsStage = new Stage();
        initializePopUpScene(settingsStage, settingsScene, Icon.SETTINGS);
        initializeFullScreenButton(mainPane);
        initializeLogoutButton(mainPane, settingsStage);
        initializeCloseSettingsButton(mainPane, settingsStage);

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

    private void initializeSettingsSectionTitle(String title, Pane settingsMainPane, double layoutX, double layoutY) {
        Text sectionTitle = new Text(title);
        sectionTitle.setFont(loadTitleFont(30));
        sectionTitle.setLayoutX(layoutX);
        sectionTitle.setLayoutY(layoutY);
        settingsMainPane.getChildren().add(sectionTitle);
    }

    private void initializeLogoutButton(Pane settingsPane, Stage settingsStage) {

        Button logoutButton = new Button("Exit");
        Font liberationSans = loadFontLiberationSansRegular(15);
        if(liberationSans == null){
            System.err.println("err");
        }
        logoutButton.setFont(liberationSans);
        logoutButton.setPrefSize(160, 40);
        logoutButton.setLayoutX(30);
        logoutButton.setLayoutY(200);

        ImageView logoutImage = (new ImageView(Icon.LOGOUT.getPath()));
        logoutImage.setFitHeight(20);
        logoutImage.setFitWidth(20);
        logoutButton.setGraphic(logoutImage);

        settingsPane.getChildren().add(logoutButton);

        logoutButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    getLogoutConfirmationInput(settingsStage);
                }
            }
        });
    }

    private void getLogoutConfirmationInput(Stage settingsStage) {
        Alert logoutConfirmationMessage = new Alert(Alert.AlertType.CONFIRMATION);
        logoutConfirmationMessage.setTitle("Exit");
        logoutConfirmationMessage.setHeaderText("Are you sure to close the application?");
        logoutConfirmationMessage.setContentText("You'll be able to connect and play again, with the same username if the game isn't finished");
        logoutConfirmationMessage.initOwner(settingsStage);
        Optional<ButtonType> result = logoutConfirmationMessage.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals(ButtonType.OK)) {
                System.exit(0);
            }
        }
    }

    private void initializeCommandInfoPane(Pane settingsPane) {

        double layoutY = 0.0;
        int distanceFromTitle = 23;
        int distanceFromInstructions = 50;

        Pane commandInfoPane = new Pane();
        Text setupPhase = new Text("- Setup Phase");
        setupPhase.setFont(loadFontLiberationSansRegular(18));
        commandInfoPane.getChildren().add(setupPhase);
        Text setupPhaseInstruction = new Text("  During this phase every player can only click on the cards or on the colors\n  in order to select them");

        layoutY = layoutY + distanceFromTitle;

        setupPhaseInstruction.setLayoutY(layoutY);
        setupPhaseInstruction.setFont(loadFontLiberationSansRegular(12));
        commandInfoPane.getChildren().add(setupPhaseInstruction);

        layoutY = layoutY + distanceFromInstructions;

        Text gamePhase = new Text("- Game Phase");
        gamePhase.setFont(loadFontLiberationSansRegular(18));
        gamePhase.setLayoutY(layoutY);
        commandInfoPane.getChildren().add(gamePhase);
        Text gamePhaseInstruction = new Text("""
                  This is the main phase of the game and every player can perform
                  the following actions:

                   Actions the player can perform at any time:

                    -Flip the visible side of the cards : left-click to flip other players' cards and
                     right-click to flip your own cards.

                    -Observe other playgrounds : left-click on the EYE icon in the player info-box.\s
                     Then left-click on the HOME icon or one of your cards to return
                     to your playground

                    -Open Rulebook/Settings : left-click on the BOOK/SETTINGS icon
                     in the bottom right corner of the screen (left-click again to close it). \s

                    -Send Message : select recipient from the multiple-choice menu under the chat,
                     write your message and release ENTER to send it.

                   Actions the player can perform only during his turn:

                    -Place a card: left-click on the card image to select its visible side and view
                     all empty positions. Then, left-click on an empty position to place the card.
                     To deselect the card, left-click on the card itself, or click on another card
                     to change the selection. The card's side placed is always the visible one,
                     even if the card is flipped after the selection

                    -Draw a card: left-click on one of the face up cards or one of the decks.
                     This action can only be performed after a placing a card\
                """);

        gamePhaseInstruction.setFont(loadFontLiberationSansRegular(12));


        layoutY = layoutY + distanceFromTitle;

        gamePhaseInstruction.setLayoutY(layoutY);
        commandInfoPane.getChildren().add(gamePhaseInstruction);

        layoutY = 560;


        Text suspendedGamePhase = new Text("- Suspended Game Phase");
        suspendedGamePhase.setLayoutY(layoutY);
        suspendedGamePhase.setFont(loadFontLiberationSansRegular(18));
        commandInfoPane.getChildren().add(suspendedGamePhase);
        Text suspendedGamePhaseInstruction = new Text("  During this phase every player can only click send messages");

        layoutY = layoutY + distanceFromTitle;

        suspendedGamePhaseInstruction.setLayoutY(layoutY);
        suspendedGamePhaseInstruction.setFont(loadFontLiberationSansRegular(12));
        commandInfoPane.getChildren().add(suspendedGamePhaseInstruction);

        Text creditText = new Text("  Icons by Icons8");
        creditText.setLayoutY(1030);
        creditText.setFont(loadFontLiberationSansRegular(12));
        creditText.setLayoutX(30);
        settingsPane.getChildren().add(creditText);

        commandInfoPane.setLayoutX(30);
        commandInfoPane.setLayoutY(400);

        settingsPane.getChildren().add(commandInfoPane);
    }

    private void initializeCloseSettingsButton(Pane settingsPane, Stage settingsStage) {
        Button closeSettingsButton = new Button("Close Settings");
        closeSettingsButton.setFont(new Font(CAMBRIA_MATH, 15));
        closeSettingsButton.setPrefSize(160, 40);
        closeSettingsButton.setLayoutX(30);
        closeSettingsButton.setLayoutY(150);

        ImageView closeSettingsImage = (new ImageView(Icon.RETURN.getPath()));
        closeSettingsImage.setFitHeight(20);
        closeSettingsImage.setFitWidth(20);
        closeSettingsButton.setGraphic(closeSettingsImage);

        settingsPane.getChildren().add(closeSettingsButton);

        closeSettingsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    settingsStage.close();
                    settingsOpened = false;
                }
            }
        });
    }

    private void initializeFullScreenButton(Pane mainPane) {

        Pane fullscreenPane = new Pane();
        fullscreenPane.setPrefSize(190, 40);
        fullscreenPane.setLayoutX(30);
        fullscreenPane.setLayoutY(100);

        Button fullscreenButton = new Button("Full-Screen Mode");
        fullscreenButton.setFont(new Font(CAMBRIA_MATH, 15));
        fullscreenButton.setPrefSize(160, 40);

        ImageView fullscreenImage = (new ImageView(Icon.FULLSCREEN.getPath()));
        fullscreenImage.setFitHeight(20);
        fullscreenImage.setFitWidth(20);
        fullscreenButton.setGraphic(fullscreenImage);

        Text fullScreenStatus = new Text("OFF");
        fullScreenStatus.setFont(new Font(CAMBRIA_MATH, 15));
        fullScreenStatus.setLayoutX(165);
        fullScreenStatus.setLayoutY(25);
        fullscreenPane.getChildren().add(fullscreenButton);
        fullscreenPane.getChildren().add(fullScreenStatus);
        mainPane.getChildren().add(fullscreenPane);

        fullscreenButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    if (gui.getIsFullScreen()) {
                        gui.setWindowScreenMode();
                        fullScreenStatus.setText("OFF");
                    } else {
                        gui.setFullScreenMode();
                        fullScreenStatus.setText("ON");
                    }
                }
            }
        });
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


    protected StackPane generateError(String details) {
        StackPane errorPane = new StackPane();
        errorPane.setPrefSize(errorPaneWidth, errorPaneHeight);
        errorPane.setStyle("-fx-background-color: #dd2d2a;" + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0.2, 0, 0);" + " -fx-background-radius: 10px;");
        errorPane.setOpacity(0.6);
        Label errorMessage = new Label();
        errorMessage.setStyle("-fx-text-fill: #ffffff;" + "-fx-font-weight: bold;");
        errorMessage.setWrapText(true);
        errorPane.getChildren().add(errorMessage);
        StackPane.setAlignment(errorMessage, Pos.CENTER);
        errorMessage.setText(details);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(2),
                timerEndEvent -> fadeOutUpdatePane(errorPane)
        ));
        timeline.play();

        return errorPane;
    }

    protected void fadeOutUpdatePane(StackPane updatePane) {
        FadeTransition transition = new FadeTransition(Duration.seconds(1), updatePane);
        transition.setFromValue(updatePane.getOpacity());
        transition.setToValue(0);
        transition.setOnFinished(actionEvent -> removeUpdatePaneFromMainPane(updatePane));
        transition.play();
    }

    protected abstract void removeUpdatePaneFromMainPane(StackPane errorPane);

    public abstract void showError(String details);

}
