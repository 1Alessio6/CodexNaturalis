package it.polimi.ingsw.network.client.view.drawplayground;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Face;
import it.polimi.ingsw.model.card.Front;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.view.tui.drawplayground.DrawablePlayground;
import it.polimi.ingsw.network.client.view.tui.drawplayground.InvalidCardDimensionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DrawablePlaygroundTest {
    private final int cardWidth = 7;
    private final int cardHeight = 3;


    @Test
    void drawStarter_doesNotThrow() throws InvalidCardDimensionException {
        Playground playground = new Playground();
        Position starterPosition = new Position(0, 0);
        String[][] starterRep = new String[cardHeight][cardWidth];
        Map<CornerPosition, Corner> corners = new HashMap<>();
        corners.put(CornerPosition.TOP_LEFT, new Corner());
        Front starterFront = new Front(CardColor.BLUE, corners, 0);
        Assertions.assertDoesNotThrow(
                () -> {
                    playground.placeCard(starterFront, new Position(0, 0));
                });
        ClientPlayground clientPlayground = new ClientPlayground(playground);
        DrawablePlayground drawablePlayground = new DrawablePlayground(cardWidth, cardHeight);
        drawablePlayground.allocateMatrix(clientPlayground);
        Assertions.assertDoesNotThrow(() -> {
            drawablePlayground.drawCard(starterPosition, starterRep);
        });
    }
}