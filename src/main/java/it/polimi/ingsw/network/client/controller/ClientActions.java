package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.card.NotExistingFaceUp;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.network.VirtualView;

public interface ClientActions {

    void connect(VirtualView client, String username) throws InvalidUsernameException;
    void placeCard(int cardHandPosition, Side selectedSide, Position position) throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException;
    void draw(int IdToDraw) throws  InvalidGamePhaseException, EmptyDeckException, NotExistingFaceUp, SuspendedGameException;

    void placeStarter(Side side) throws SuspendedGameException;

    void chooseColor(PlayerColor color) throws InvalidColorException, SuspendedGameException;

    void placeObjectiveCard(int chosenObjective) throws SuspendedGameException;

    void sendMessage(Message message) throws InvalidMessageException;

    void setPlayersNumber(int playersNumber);

}
