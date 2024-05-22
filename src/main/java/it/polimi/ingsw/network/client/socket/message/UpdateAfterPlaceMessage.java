package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.client.model.card.ClientCard;

import java.util.List;
import java.util.Map;

public class UpdateAfterPlaceMessage extends NetworkMessage {

    private final Map<Position, CornerPosition> positionToCornerCovered;

    private final List<Position> newAvailablePositions;

    private final Map<Symbol, Integer> newResources;

    private final int points;

    private final String username;

    private final ClientCard placedCard;

    private final Side placedSide;

    private final Position position;


    public UpdateAfterPlaceMessage(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) {
        super(Type.SHOW_UPDATE_AFTER_PLACE, "server");
        this.positionToCornerCovered = positionToCornerCovered;
        this.newAvailablePositions = newAvailablePositions;
        this.newResources = newResources;
        this.points = points;
        this.username = username;
        this.placedCard = placedCard;
        this.placedSide = placedSide;
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public ClientCard getPlacedCard() {
        return placedCard;
    }

    public int getPoints() {
        return points;
    }

    public List<Position> getNewAvailablePositions() {
        return newAvailablePositions;
    }

    public Map<Position, CornerPosition> getPositionToCornerCovered() {
        return positionToCornerCovered;
    }

    public Map<Symbol, Integer> getNewResources() {
        return newResources;
    }

    public Position getPosition() {
        return position;
    }

    public Side getPlacedSide() {
        return placedSide;
    }
}
