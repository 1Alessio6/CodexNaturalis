package it.polimi.ingsw.network.server.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.InvalidIdForDrawingException;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.card.InvalidCardIdException;
import it.polimi.ingsw.model.card.InvalidFaceUpCardException;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.server.socket.message.*;

import java.io.PrintWriter;
import java.rmi.RemoteException;

public class ServerProxy implements VirtualServer {
    private static final Gson gson = new Gson();
    private final PrintWriter out;

    public ServerProxy(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void connect(VirtualView client, String username) throws RemoteException, FullLobbyException, InvalidUsernameException {
        ConnectMessage message = new ConnectMessage(username);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void placeStarter(String username, Side side) throws InvalidPlayerActionException, InvalidGamePhaseException, RemoteException {
        PlaceStarterMessage message = new PlaceStarterMessage(username, side);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void chooseColor(String username, PlayerColor color) throws NonexistentPlayerException, InvalidColorException, InvalidPlayerActionException, InvalidGamePhaseException, RemoteException {
        ChooseColorMessage message = new ChooseColorMessage(username, color);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException, InvalidGamePhaseException, RemoteException {
        PlaceObjectiveMessage message = new PlaceObjectiveMessage(username, chosenObjective);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void placeCard(String username, int frontId, int backId, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, RemoteException, InvalidCardIdException {
        PlaceCardMessage message = new PlaceCardMessage(username, frontId, backId, side, position);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void draw(String username, int idToDraw) throws InvalidPlayerActionException, EmptyDeckException, InvalidGamePhaseException, RemoteException, InvalidIdForDrawingException, InvalidFaceUpCardException {
        DrawMessage message = new DrawMessage(username, idToDraw);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void sendMessage(Message chatMessage) throws InvalidMessageException, RemoteException {
        SendChatMessage message = new SendChatMessage(chatMessage.getSender(), chatMessage);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void setPlayersNumber(String username, int playersNumber) throws RemoteException, InvalidPlayersNumberException {
        SetPlayerNumberMessage message = new SetPlayerNumberMessage(username, playersNumber);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void disconnect(String username) throws InvalidUsernameException, RemoteException {
        DisconnectMessage message = new DisconnectMessage(username);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void sendPing(String username) throws RemoteException {
        PingMessage message = new PingMessage(username);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }
}
