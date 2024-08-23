package it.polimi.ingsw.network.client.view.tui;

import it.polimi.ingsw.model.board.Position;

import static it.polimi.ingsw.network.client.view.tui.ClientUtil.cardHeight;
import static it.polimi.ingsw.network.client.view.tui.ClientUtil.cardWidth;

/**
 * This enum represents the space and position of each area in the screen
 */
public enum GameScreenArea {
    // Matrix (12-1) * (20-1) cards (considering last not overlapping)
    PLAYGROUND(97, 41, new Position(27, 3)),
    FACE_UP_CARDS(24, 14, new Position(146, 2)),
    HAND_CARDS(2 * ClientUtil.areaPadding + 3 * cardWidth, cardHeight, new Position(62, 44)),
    DECKS(24, 5, new Position(18, 149)),
    CHAT(62, 11, new Position(23, 126)),
    INPUT_AREA(62, 12, new Position(35, 126)),
    TITLE(80, 5, new Position(2, 55)),
    SCOREBOARD(10, 24, new Position(2, 2)),
    PRIVATE_OBJECTIVE(ClientUtil.objectiveCardWidth, ClientUtil.objectiveCardHeight, new Position(35, 2)),
    COMMON_OBJECTIVE(2 + 2 * ClientUtil.objectiveCardWidth, ClientUtil.objectiveCardHeight, new Position(42, 2)),
    RESOURCES(26, 15, new Position(14, 2)),
    NOTIFICATIONS(PLAYGROUND.width, 1, new Position(1,50));

    final int width;
    final int height;
    final Position screenPosition;

    GameScreenArea(int width, int height, Position screenPosition) {
        this.width = width;
        this.height = height;
        this.screenPosition = screenPosition;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Position getScreenPosition() {
        return screenPosition;
    }
}
