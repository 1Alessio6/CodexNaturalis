package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.color.PlayerColor;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.Message;

import java.net.ServerSocket;

/**
 * Server notifies the controller of changes made by the player in the view when Socket communication is used.
 */
public class ServerForSocket {
    private final ServerSocket listenSocket;

    private Controller controller;

    public ServerForSocket(ServerSocket listenSocket) {
        this.listenSocket = listenSocket;
        this.controller = new Controller();
    }

    /**
     * Method used to connect an <code>username</code> to a <code>client</code>.
     *
     * @param client   the representation of the client.
     * @param username the username of the player to connect.
     */
    public void connect(SocketHandler client, String username) {
        controller.handleConnection(username, client);
    }

    /**
     * Method used to disconnect an <code>username</code> from the game.
     *
     * @param username the username of the player to disconnect.
     */
    public void disconnect(String username) {
        controller.handleDisconnection(username);
    }

    /**
     * Method used to place the starter card of the <code>username</code> in a specific <code>side</code>.
     *
     * @param username the username of the player who performs the placement.
     * @param side     the selected side of the starter card.
     */
    public void placeStarter(String username, Side side) {
        controller.placeStarter(username, side);
    }

    /**
     * Method used to assign the <code>color</code> to the <code>username</code>.
     *
     * @param username the username of the player who has chosen the color.
     * @param color    the color chosen by the player.
     */
    public void chooseColor(String username, PlayerColor color) {
        controller.chooseColor(username, color);
    }

    /**
     * Method used to place the <code>chosenObjective</code> of the <code>username</code>.
     *
     * @param username        the username of the player.
     * @param chosenObjective the objective card chosen by the player.
     */
    public void placeObjectiveCard(String username, int chosenObjective) {
        controller.placeObjectiveCard(username, chosenObjective);
    }

    /**
     * Method used to place a card in a specific <code>frontId</code>,<code>backId</code>, <code>side</code> and
     * <code>position</code>.
     *
     * @param username the username of the player.
     * @param frontId  the identification of the front of the card.
     * @param backId   the identification of the back of the card.
     * @param side     the selected side of the card.
     * @param position the selected position to place the card.
     */
    public void placeCard(String username, int frontId, int backId, Side side, Position position) {
        controller.placeCard(username, frontId, backId, side, position);
    }

    /**
     * Method used to draw a card.
     *
     * @param username the username of the player.
     * @param idToDraw the identification of the card to draw.
     */
    public void draw(String username, int idToDraw) {
        controller.draw(username, idToDraw);
    }

    /**
     * Method used to send a new <code>message</code>
     *
     * @param message the message to send.
     */
    public void sendMessage(Message message) {
        controller.sendMessage(message);
    }

    /**
     * Method used to choose the <code>playersNumber</code> of the game.
     *
     * @param username      the username of the player who chooses the <code>playersNumber</code>.
     * @param playersNumber the number of players allowed in the game.
     */
    public void setPlayersNumber(String username, int playersNumber) {
        controller.setPlayersNumber(username, playersNumber);
    }
}
