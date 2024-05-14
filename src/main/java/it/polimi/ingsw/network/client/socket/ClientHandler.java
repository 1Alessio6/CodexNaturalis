package it.polimi.ingsw.network.client.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.InvalidIdForDrawingException;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.socket.message.*;
import it.polimi.ingsw.network.server.socket.Server;
import it.polimi.ingsw.network.server.socket.message.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientHandler implements VirtualView {

    private final Server server;
    private final PrintWriter out;
    private final BufferedReader input;

    private final Gson gson;

    public ClientHandler(Server server, BufferedReader input, PrintWriter out) {
        this.server = server;
        this.out = out;
        this.input = input;
        gson = new Gson();
    }

    public void run() throws IOException {
        String line = input.readLine();
        while (line != null) {
            ServerMessage message = gson.fromJson(line, ServerMessage.class);
            ServerType type = message.getType();
            String sender = message.getSender();


            switch (type) {
                case CONNECT:
                    assert message instanceof ConnectMessage;
                    ConnectMessage connectMessage = (ConnectMessage) message;

                    try {
                        server.connect(connectMessage.getClient(), sender);
                    } catch (FullLobbyException | InvalidUsernameException e) {
                        System.err.println(e.getMessage());
                    }

                case PLACE_STARTER:
                    assert message instanceof PlaceStarterMessage;
                    PlaceStarterMessage placeStarterMessage = (PlaceStarterMessage) message;
                    try {
                        server.placeStarter(sender, placeStarterMessage.getSide());
                    } catch (InvalidPlayerActionException | InvalidGamePhaseException e) {
                        System.err.println(e.getMessage());
                    }

                case CHOOSE_COLOR:
                    assert message instanceof ChooseColorMessage;
                    ChooseColorMessage chooseColorMessage = (ChooseColorMessage) message;

                    try {
                        server.chooseColor(sender, chooseColorMessage.getColor());
                    } catch (NonexistentPlayerException | InvalidGamePhaseException | InvalidPlayerActionException |
                             InvalidColorException e) {
                        System.err.println(e.getMessage());
                    }

                case PLACE_OBJECTIVE:
                    assert message instanceof PlaceObjectiveMessage;
                    PlaceObjectiveMessage placeObjectiveMessage = (PlaceObjectiveMessage) message;

                    try {
                        server.placeObjectiveCard(sender, placeObjectiveMessage.getChosenObjective());
                    } catch (InvalidPlayerActionException | InvalidGamePhaseException e) {
                        throw new RuntimeException(e);
                    }

                case PLACE_CARD:
                    assert message instanceof PlaceCardMessage;
                    PlaceCardMessage placeCardMessage = (PlaceCardMessage) message;
                    try {
                        server.placeCard(sender, placeCardMessage.getFrontId(), placeCardMessage.getBackId(),
                                placeCardMessage.getSide(), placeCardMessage.getPosition());
                    } catch (InvalidPlayerActionException | Playground.UnavailablePositionException |
                             Playground.NotEnoughResourcesException | InvalidGamePhaseException |
                             SuspendedGameException | InvalidCardIdException e) {
                        System.err.println(e.getMessage());
                    }

                case DRAW:
                    assert message instanceof DrawMessage;
                    DrawMessage drawMessage = (DrawMessage) message;

                    try {
                        server.draw(sender, drawMessage.getIdDraw());
                    } catch (InvalidGamePhaseException | InvalidPlayerActionException | EmptyDeckException |
                             InvalidIdForDrawingException | InvalidFaceUpCardException e) {
                        System.err.println(e.getMessage());
                    }

                case SEND_CHAT_MESSAGE:
                    assert message instanceof SendChatMessage;
                    SendChatMessage sendChatMessage = (SendChatMessage) message;

                    try {
                        server.sendMessage(sendChatMessage.getMessage());
                    } catch (InvalidMessageException e) {
                        System.err.println(e.getMessage());
                    }

                case SET_PLAYER_NUMBER:
                    assert message instanceof SetPlayerNumberMessage;
                    SetPlayerNumberMessage setPlayerNumberMessage = (SetPlayerNumberMessage) message;

                    try {
                        server.setPlayersNumber(sender, setPlayerNumberMessage.getNumPlayers());
                    } catch (InvalidPlayersNumberException e) {
                        System.err.println(e.getMessage());
                    }

                case DISCONNECT:
                    try {
                        server.disconnect(sender);
                    } catch (RemoteException | InvalidUsernameException e) {
                        System.err.println(e.getMessage());
                    }


                case SEND_PING:
                    server.sendPing(sender);
            }

            line = input.readLine();
        }
    }

    @Override
    public void updateCreator() throws RemoteException {
        UpdateCreatorMessage message = new UpdateCreatorMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void updateAfterLobbyCrash() throws RemoteException {
        UpdateAfterLobbyCrashMessage message = new UpdateAfterLobbyCrashMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void updateAfterConnection(ClientGame clientGame) throws RemoteException {
        UpdateAfterConnectionMessage message = new UpdateAfterConnectionMessage(clientGame);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {
        UpdatePlayersInLobbyMessage message = new UpdatePlayersInLobbyMessage(usernames);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        UpdatePlayerStatusMessage message = new UpdatePlayerStatusMessage(isConnected, username);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        UpdateColorMessage message = new UpdateColorMessage(username, color);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateObjectiveCard(ClientCard chosenObjective, String username) throws RemoteException {
        UpdateObjectiveCardMessage message = new UpdateObjectiveCardMessage(chosenObjective, username);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {
        UpdateAfterPlaceMessage message = new UpdateAfterPlaceMessage(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {
        UpdateAfterDrawMessage message = new UpdateAfterDrawMessage(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        UpdateChatMessage clientMessage = new UpdateChatMessage(message);
        String jsonMessage = gson.toJson(clientMessage);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {
        UpdateCurrentPlayerMessage message = new UpdateCurrentPlayerMessage(currentPlayerIdx, phase);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateSuspendedGame() throws RemoteException {
        UpdateSuspendedGameMessage message = new UpdateSuspendedGameMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showWinners(List<String> winners) throws RemoteException {
        ShowWinnersMessage message = new ShowWinnersMessage(winners);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void reportError(String details) throws RemoteException {
        ReportErrorMessage message = new ReportErrorMessage(details);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }


}
