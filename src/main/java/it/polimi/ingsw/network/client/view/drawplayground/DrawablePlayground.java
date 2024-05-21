package it.polimi.ingsw.network.client.view.drawplayground;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.board.ClientTile;

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
        Position startingPoint = new Position(0, 0);
        int xMax = 0;
        int yMax = 0;

        Queue<Position> positionOfTileToVisit = new LinkedList<>();
        Set<Position> alreadyCheckedPosition = new HashSet<>();
        positionOfTileToVisit.add(startingPoint);
        while (!positionOfTileToVisit.isEmpty()) {
            Position pos = positionOfTileToVisit.remove();
            int xPosAbs = Math.abs(pos.getX());
            int yPosAbs = Math.abs(pos.getY());

            if (xMax < xPosAbs) {
                xMax = xPosAbs;
            }
            if (yMax < yPosAbs) {
                yMax = yPosAbs;
            }

            List<Position> offsets = Position.getOffsets();
            for (Position offset : offsets) {
                Position possiblePos = Position.sum(pos, offset);
                ClientTile tile = playground.getTile(possiblePos);
                if (tile != null && !alreadyCheckedPosition.contains(possiblePos)) {
                    positionOfTileToVisit.add(possiblePos);
                }
            }
            alreadyCheckedPosition.add(pos);
        }
        return new int[]{xMax, yMax};
    }

    private Position calcCenterPosition(int xMax, int yMax) {
        int xCenter = xMax * (cardWidth - 1) + (cardWidth - 1) / 2;
        int yCenter = yMax * (cardHeight - 1) + (cardHeight - 1) / 2;
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
        matPlayground = new String[matrixWidth][matrixHeight];
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
        int x = centerPosition.getX() + (cardWidth - 1) * cardPos.getX();
        int y = centerPosition.getY() + (1 - cardHeight) * cardPos.getY();
        return new Position(x, y);
    }

    private Map<Position, CornerPosition> getMapFromPositionToCornerPosition() {
        Map<Position, CornerPosition> positionToCornerPosition = new HashMap<>();

        positionToCornerPosition.put(
                new Position(0, 0),
                CornerPosition.TOP_LEFT
        );

        positionToCornerPosition.put(
                new Position(cardWidth - 1, 0),
                CornerPosition.TOP_RIGHT
        );

        positionToCornerPosition.put(
                new Position(cardWidth - 1, cardHeight - 1),
                CornerPosition.LOWER_RIGHT
        );

        positionToCornerPosition.put(
                new Position(0, cardHeight - 1),
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
        int yUpperLeftCorner = remappedPos.getY() - (cardHeight - 1) / 2;
        int xUpperLeftCorner = remappedPos.getX() - (cardWidth - 1) / 2;
        for (int i = yUpperLeftCorner; i < yUpperLeftCorner + cardHeight; ++i) {
            for (int j = xUpperLeftCorner; j < xUpperLeftCorner + cardWidth; ++j) {
                ClientTile cardTile = clientPlayground.getTile(cardPos);
                int xRelativeToUpperLeftCorner = i - xUpperLeftCorner;
                int yRelativeToUpperLeftCorner = j - yUpperLeftCorner;
                CornerPosition cornerPosition = positionToCornerPosition.get(new Position(xRelativeToUpperLeftCorner, yRelativeToUpperLeftCorner));
                // skip if the corner of the card is covered, therefore its symbol has to be hidden
                if (cornerPosition != null
                        && cardTile.getFace().getCorners().get(cornerPosition).isCovered()) {
                    continue;
                }
                matPlayground[i][j] = card[xRelativeToUpperLeftCorner][yRelativeToUpperLeftCorner];
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
