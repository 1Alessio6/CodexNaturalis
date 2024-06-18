package it.polimi.ingsw.network.client.view.tui.drawplayground;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;


public class DrawablePlayground {
    String[][] matPlayground;
    int cardWidth;
    int cardHeight;

    // playground has min references: start to print to the min
    // be careful: yMax in playground has the lowest coordinate in the screen, as it's printed top-bottom
    // these coordinates represent the limits of the PRINTABLE PLAYGROUND, not real one
    Position upperLeft;
    Position lowerRight;

    // offset needed by tui
    Position currentOffset;

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
     * @param limitPositions considered (in order: upper left and lower right)
     * @return the dimension (minimum value is 1)
     */
    public static int[] calculateSizes(Position[] limitPositions) {
        return new int[] {limitPositions[1].getX() - limitPositions[0].getX() + 1,
                limitPositions[0].getY() - limitPositions[1].getY() + 1};
    }

    /**
     * Method used to create the base where card will lay
     * @param sizes of the matrix
     */
    public void allocateMatrix(int[] sizes) {
        int matrixWidth = (sizes[0] - 1) * (cardWidth - 1) + cardWidth;
        int matrixHeight = (sizes[1] - 1) * (cardHeight - 1) + cardHeight;
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

    /**
     * Sets the temporary printable upper left and lower right (consider also the old offset)
     * @param numOfOverflowing tiles
     * @param realUpperLeft, needed to calculate the printable upper left
     * @param offset, to add to centered offset
     */
    public void setStartPrintPos(int[] numOfOverflowing, Position realUpperLeft, int[] finalPlaygroundSizes,
                                 Position offset){
        this.upperLeft = Position.sum(new Position(realUpperLeft.getX() + numOfOverflowing[0] / 2,
                realUpperLeft.getY() - numOfOverflowing[1] / 2),
                offset);

        // displace of one: this is considered with the upper left position
        this.lowerRight = Position.sum(upperLeft, new Position(finalPlaygroundSizes[0] - 1, 1 - finalPlaygroundSizes[1]));

        this.currentOffset = offset;
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
        int x = (cardPos.getX() - upperLeft.getX()) * (cardWidth - 1);
        // be careful: yMax in playground has the lowest coordinate in the screen, as it's printed top-bottom
        int y = (cardPos.getY() - upperLeft.getY()) * (1 - cardHeight); // invert sign, as playground's y grows bottom-top
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

    /**
     * @return the upper left and lower right corner
     */
    public Position[] getLimitPositions() {
        return new Position[]{upperLeft, lowerRight};
    }

    public Position getCurrentOffset() {
        return currentOffset;
    }
}
