package it.polimi.ingsw.network.client.view.drawplayground;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.board.ClientTile;
import it.polimi.ingsw.network.client.view.tui.ClientUtil;

import java.util.*;


public class DrawablePlayground {
    String[][] matPlayground;
    Position centerPosition;
    int cardWidth;
    int cardHeight;

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

    private int[] getRange(ClientPlayground playground) {
        int xMax = 0;
        int yMax = 0;
        List<Position> positionsToNavigate = new ArrayList<>();
        List<Position> occupiedPositions = new ArrayList<>(playground.getAllPositions());
        List<Position> availablePositions = playground.getAvailablePositions();
        positionsToNavigate.addAll(occupiedPositions);
        positionsToNavigate.addAll(availablePositions);

        for (Position pos : positionsToNavigate) {
            int xPosAbs = Math.abs(pos.getX());
            int yPosAbs = Math.abs(pos.getY());

            if (xMax < xPosAbs) {
                xMax = xPosAbs;
            }

            if (yMax < yPosAbs) {
                yMax = yPosAbs;
            }
        }

        return new int[]{xMax, yMax};
    }

    private Position calcCenterPosition(int xMax, int yMax) {
        int yCenter = xMax * (cardWidth - 1) + (cardWidth - 1) / 2;
        int xCenter = yMax * (cardHeight - 1) + (cardHeight - 1) / 2;
        return new Position(xCenter, yCenter);
    }

    private int calcMatrixWidth(int xMax) {
        return 2 * (xMax * (cardWidth - 1) + (cardWidth - 1) / 2 + 1) - 1;
    }

    private int calcMatrixHeight(int yMax) {
        return 2 * (yMax * (cardHeight - 1) + (cardHeight - 1) / 2 + 1) - 1;
    }

    public void allocateMatrix(ClientPlayground playground) {
        int[] range = getRange(playground);
        int xMax = range[0];
        int yMax = range[1];
        centerPosition = calcCenterPosition(xMax, yMax);
        int matrixWidth = calcMatrixWidth(xMax);
        int matrixHeight = calcMatrixHeight(yMax);

        // initialize empty spaces, so there are no null strings
        matPlayground = ClientUtil.createEmptyArea(matrixHeight, matrixWidth);
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

    private Position calcPosInMatrix(Position cardPos) {
        int y = centerPosition.getY() + (cardWidth - 1) * cardPos.getX();
        int x = centerPosition.getX() + (1 - cardHeight) * cardPos.getY();
        return new Position(x, y);
    }

    private Map<Position, CornerPosition> getMapFromPositionToCornerPosition() {
        Map<Position, CornerPosition> positionToCornerPosition = new HashMap<>();

        positionToCornerPosition.put(
                new Position(0, 0),
                CornerPosition.TOP_LEFT
        );

        positionToCornerPosition.put(
                new Position(0, cardWidth - 1),
                CornerPosition.TOP_RIGHT
        );

        positionToCornerPosition.put(
                new Position(cardHeight - 1, cardWidth - 1),
                CornerPosition.LOWER_RIGHT
        );

        positionToCornerPosition.put(
                new Position(cardHeight - 1, 0),
                CornerPosition.LOWER_LEFT
        );

        return positionToCornerPosition;
    }

    public void drawCard(ClientPlayground clientPlayground, Position cardPos, String[][] card) throws InvalidCardRepresentationException {
        if (!validCardRepresentation(card)) {
            throw new InvalidCardRepresentationException(cardPos.getX(), cardPos.getY());
        }

        Position remappedPos = calcPosInMatrix(cardPos);
        Map<Position, CornerPosition> positionToCornerPosition = getMapFromPositionToCornerPosition();
        int yUpperLeftCorner = remappedPos.getY() - (cardWidth - 1) / 2;
        int xUpperLeftCorner = remappedPos.getX() - (cardHeight - 1) / 2;

        for (int i = xUpperLeftCorner; i < xUpperLeftCorner + cardHeight; ++i) {
            for (int j = yUpperLeftCorner; j < yUpperLeftCorner + cardWidth; ++j) {
                ClientTile cardTile = clientPlayground.getTile(cardPos);
                int xRelativeToUpperLeftCorner = i - xUpperLeftCorner;
                int yRelativeToUpperLeftCorner = j - yUpperLeftCorner;
                CornerPosition cornerPosition = positionToCornerPosition.get(new Position(xRelativeToUpperLeftCorner, yRelativeToUpperLeftCorner));
                // skip if the corner of the card is covered, therefore its symbol has to be hidden
                // todo: improve
                Map<CornerPosition, Corner> fakeCorners = new HashMap<>();
                for (CornerPosition cp : CornerPosition.values()) {
                    fakeCorners.put(cp, new Corner());
                }
                Map<CornerPosition, Corner> faceCorners = cardTile.sameAvailability(Availability.EMPTY) ? fakeCorners : cardTile.getFace().getCorners();
                if (!(cornerPosition != null // can be null?
                        && faceCorners.containsKey(cornerPosition)
                        && faceCorners.get(cornerPosition).isCovered())) {
                    matPlayground[i][j] = card[xRelativeToUpperLeftCorner][yRelativeToUpperLeftCorner];
                }
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
