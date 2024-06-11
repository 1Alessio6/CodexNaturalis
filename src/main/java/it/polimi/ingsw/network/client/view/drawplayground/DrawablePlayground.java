package it.polimi.ingsw.network.client.view.drawplayground;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;

import java.util.*;


public class DrawablePlayground {
    String[][] matPlayground;
    int cardWidth;
    int cardHeight;

    // playground has min references: start to print to the min
    // be careful: yMax in playground has the lowest coordinate in the screen, as it's printed top-bottom
    int xMax;
    int xMin;
    int yMax;
    int yMin;

    private boolean validDimension(int cardWidth, int cardHeight) {
        // odd dimension to have a center
        return cardWidth % 2 == 1
                && cardHeight % 2 == 1;
    }

    public DrawablePlayground(int cardWidth, int cardHeight) throws InvalidCardDimensionException {
        if (!validDimension(cardWidth, cardHeight)) {
            throw new InvalidCardDimensionException();
        }
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
    }

    private int calculateSizes(int max, int min) {
        return max - min + 1;
    }

    public void allocateMatrix(ClientPlayground playground) {
        int[] xs = playground.getXMaxAndMin();
        int[] ys = playground.getYMaxAndMin();
        xMax = xs[0];
        xMin = xs[1];
        yMax = ys[0];
        yMin = ys[1];

        int playgroundWidth = calculateSizes(xMax, xMin);
        int playgroundHeight = calculateSizes(yMax, yMin);
        int matrixWidth = (playgroundWidth - 1) * (cardWidth - 1) + cardWidth;
        int matrixHeight = (playgroundHeight - 1) * (cardHeight - 1) + cardHeight;
        // initialize empty spaces, so there are no null strings
        matPlayground = ClientUtil.createEmptyArea(matrixHeight, matrixWidth);

        for (int y = 0; y < matrixHeight; y++) {
            // all cards but least overlap on corner, so they have "one" cell less
            // doesn't apply to last card
            int x;
            for (x = 0; x < matrixWidth - cardWidth; x += cardWidth - 1) {
                matPlayground[y][x] += " ";
            }
            // last card doesn't overlap: count last space
            matPlayground[y][x + 1] += " ";
        }
    }

    private boolean validCardRepresentation(String[][] card) {
        if (cardHeight < card.length) {
            return false;
        }

        for (String[] cardRow : card) {
            if (cardWidth < cardRow.length) {
                return false;
            }
        }
        return true;
    }

    // calculates the position of the upper left corner
    private Position calcPosInMatrix(Position cardPos) {
        int x = (cardPos.getX() - xMin) * (cardWidth - 1);
        // be careful: yMax in playground has the lowest coordinate in the screen, as it's printed top-bottom
        int y = (cardPos.getY() - yMax) * (1 - cardHeight); // invert sign, as playground's y grows bottom-top
        return new Position(x, y);
    }

    public void drawCard(Position cardPos, String[][] card) throws InvalidCardRepresentationException {
        if (!validCardRepresentation(card)) {
            throw new InvalidCardRepresentationException(cardPos.getX(), cardPos.getY());
        }

        Position remappedPos = calcPosInMatrix(cardPos);
        for (int y = 0; y < cardHeight; ++y) {
            for (int x = 0; x < cardWidth; ++x) {
                matPlayground[y + remappedPos.getY()][x + remappedPos.getX()] = card[y][x];
            }
        }
    }

    public String[][] getPlaygroundRepresentation() throws UnInitializedPlaygroundException {
        if (matPlayground == null) {
            throw new UnInitializedPlaygroundException();
        }
        return matPlayground;
    }
}
