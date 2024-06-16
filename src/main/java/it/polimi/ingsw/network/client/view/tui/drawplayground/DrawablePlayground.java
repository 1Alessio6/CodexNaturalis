package it.polimi.ingsw.network.client.view.tui.drawplayground;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;


public class DrawablePlayground {
    String[][] matPlayground;
    int cardWidth;
    int cardHeight;

    // playground has min references: start to print to the min
    // be careful: yMax in playground has the lowest coordinate in the screen, as it's printed top-bottom
    // 0 : max, 1 : min
    int[] xs;
    int[] ys;

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

    /**
     * Method used to get the range of xs or ys
     * @param coordinates considered (in order: max and min)
     * @return the dimension (minimum value is 1)
     */
    public static int calculateSizes(int[] coordinates) {
        return coordinates[0] - coordinates[1] + 1;
    }

    /**
     * Method used to create the base where card will lay
     * @param playground: needed to check if it overflows screen area
     * @return number of xs and ys that will overflow the screen (if present)
     */
    public int[] allocateMatrix(ClientPlayground playground) {
        xs = playground.getXMaxAndMin();
        ys = playground.getYMaxAndMin();

        int[] entirePlaygroundSizes = new int[]{calculateSizes(xs), calculateSizes(ys)};
        int[] maxPrintablePlaygroundSizes = ClientUtil.maxPlaygroundScreenPositions();

        // make the playground fit
        // considering only overlapping cards (the last not overlapping is counted in the integer division)
        int finalPlaygroundWidth =  Math.min(maxPrintablePlaygroundSizes[0], entirePlaygroundSizes[0]);
        int finalPlaygroundHeight =  Math.min(maxPrintablePlaygroundSizes[1], entirePlaygroundSizes[1]);

        int matrixWidth = (finalPlaygroundWidth - 1) * (cardWidth - 1) + cardWidth;
        int matrixHeight = (finalPlaygroundHeight - 1) * (cardHeight - 1) + cardHeight;
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

        // count positions to remove along x and y. Don't remove if playground fits the screen area
        return new int[]{Math.max(entirePlaygroundSizes[0] - maxPrintablePlaygroundSizes[0], 0),
                Math.max(entirePlaygroundSizes[1] - maxPrintablePlaygroundSizes[1], 0)};
    }

    /**
     * Method to build playground exactly centered
     * @param numOfOverflowing tiles
     * @return the upper left position, that will leave exactly half of the overflowing position along x and y.
     *          If no overflowing along a direction, that coordinate will be the same
     */
    public Position centeredStartPrintPos(int[] numOfOverflowing){
        return new Position(xs[1] + numOfOverflowing[0] / 2,
                ys[0] - numOfOverflowing[1] / 2);
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
        int x = (cardPos.getX() - xs[1]) * (cardWidth - 1);
        // be careful: yMax in playground has the lowest coordinate in the screen, as it's printed top-bottom
        int y = (cardPos.getY() - ys[0]) * (1 - cardHeight); // invert sign, as playground's y grows bottom-top
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

    public int[] getXs() {
        return xs;
    }

    public int[] getYs() {
        return ys;
    }
}
