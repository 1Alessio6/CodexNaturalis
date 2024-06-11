package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.controller.InvalidIdForDrawingException;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Availability;
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
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.board.ClientTile;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * The Client Controller modifies the model that is present in the client and the Model using the virtual server
 * in response to an action performed in the view. Furthermore, it notifies any changes to the view.
 */
public class ClientController implements ClientActions {

    private final VirtualServer server;
    private ClientGame game;

    private String mainPlayerUsername = ""; // todo. set by the view after user's input

    public ClientController(VirtualServer server) {
        this.server = server;
    }

    /**
     * Handles the connection of the <code>client</code>.
     *
     * @param client   the representation of the client.
     * @param username the client's name.
     * @throws InvalidUsernameException if the username in question has already been taken.
     * @throws RemoteException          in the event of an error occurring during the execution of a remote method.
     * @throws FullLobbyException       if the lobby is full.
     */
    @Override
    public void connect(VirtualView client, String username) throws InvalidUsernameException, RemoteException, FullLobbyException {
        server.connect(client, username);
        this.mainPlayerUsername = username;
    }

    /**
     * Places the <code>cardHandPosition</code> on the <code>selectedSide</code> and <code>position</code> specified.
     *
     * @param cardHandPosition the position of the card in the player's hand.
     * @param selectedSide     of the card.
     * @param position         in the playground at which it will be placed.
     * @throws Playground.UnavailablePositionException if the position isn't empty or isn't contained in the player's playground.
     * @throws Playground.NotEnoughResourcesException  if the resources needed to place the card aren't enough.
     * @throws InvalidGamePhaseException               if the game phase doesn't allow placing cards.
     * @throws SuspendedGameException                  if the game is suspended.
     * @throws RemoteException                         in the event of an error occurring during the execution of a remote method.
     */
    @Override
    public void placeCard(int cardHandPosition, Side selectedSide, Position position) throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, RemoteException {

        if (!game.isGameActive()) {
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if (!isMainPlayerTurn()) {
            throw new InvalidGamePhaseException("You can't place now, it's not your turn");
        }

        if (game.getCurrentPhase() != GamePhase.PlaceNormal && game.getCurrentPhase() != GamePhase.PlaceAdditional) {
            throw new InvalidGamePhaseException("You can't place now");
        }

        if (selectedSide == Side.FRONT) {
            if (!checkRequirements(getMainPlayerCard(cardHandPosition))) {
                throw new Playground.NotEnoughResourcesException("You don't have the resources to place this side of the card");
            }
        }

        if (!checkPosition(position)) {
            throw new Playground.UnavailablePositionException("The position selected isn't available");
        }

        server.placeCard(getMainPlayerUsername(), getMainPlayerCard(cardHandPosition).getFrontId(), getMainPlayerCard(cardHandPosition).getBackId(), selectedSide, position);
    }

    /**
     * Draws a card from the <code>IdToDraw</code>.
     *
     * @param IdToDraw the id representing the deck or any of the face up cards positions.
     * @throws InvalidGamePhaseException    if the game doesn't allow to draw cards.
     * @throws EmptyDeckException           in the event that the selected deck is empty.
     * @throws NotExistingFaceUp            if the face up slot is empty.
     * @throws SuspendedGameException       if the game is suspended.
     * @throws RemoteException              in the event of an error occurring during the execution of a remote method.
     * @throws InvalidIdForDrawingException if the id of the card isn't valid.
     */
    @Override
    public void draw(int IdToDraw) throws InvalidGamePhaseException, EmptyDeckException, NotExistingFaceUp, SuspendedGameException, RemoteException, InvalidIdForDrawingException {

        if (IdToDraw > 5 || IdToDraw < 0) {
            throw new InvalidIdForDrawingException();
        }

        if (!game.isGameActive()) {
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if (!isMainPlayerTurn()) {
            throw new InvalidGamePhaseException("You can't place now, it's not your turn");
        }

        if (game.getCurrentPhase() != GamePhase.DrawNormal) {
            throw new InvalidGamePhaseException("You can't draw now");
        }

        switch (IdToDraw) {
            case 0:
                if (game.isFaceUpSlotEmpty(0)) {
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
                break;
            case 1:
                if (game.isFaceUpSlotEmpty(1)) {
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
                break;
            case 2:
                if (game.isFaceUpSlotEmpty(2)) {
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
                break;
            case 3:
                if (game.isFaceUpSlotEmpty(3)) {
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
                break;
            case 4:
                if (game.getClientBoard().isGoldenDeckEmpty()) {
                    throw new EmptyDeckException("You chose to draw from an empty deck");
                }
                break;
            case 5:
                if (game.getClientBoard().isResourceDeckEmpty()) {
                    throw new EmptyDeckException("You chose to draw from an empty deck");
                }
                break;
            default:
                System.err.println("ID not valid");
                break;
        }

        server.draw(getMainPlayerUsername(), IdToDraw);
    }

    /**
     * Places the starter card of the player in the chosen <code>side</code>.
     *
     * @param side the side of the starter card.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException if the game phase doesn't allow placing cards.
     */
    @Override
    public void placeStarter(Side side) throws SuspendedGameException, RemoteException, InvalidGamePhaseException {

        if (!game.isGameActive()) {
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if (game.getCurrentPhase() != GamePhase.Setup) {
            throw new InvalidGamePhaseException("You can only place your starter card during the setup phase");
        }

        server.placeStarter(getMainPlayerUsername(), side);
    }

    /**
     * Assigns <code>color</code> to the <code>mainPlayerUsername</code>.
     *
     * @param color chosen by the <code>mainPlayerUsername</code>.
     * @throws InvalidColorException     if the color has already been selected.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException if the game phase doesn't allow to choose the <code>color</code>.
     */
    @Override
    public void chooseColor(PlayerColor color) throws InvalidColorException, SuspendedGameException, RemoteException, InvalidGamePhaseException {

        if (!game.isGameActive()) {
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if (game.getCurrentPhase() != GamePhase.Setup) {
            throw new InvalidGamePhaseException("You can only choose your color during the setup phase");
        }

        if (game.getAlreadyTakenColors().contains(color)) {
            throw new InvalidColorException("The color selected is already taken");
        }

        server.chooseColor(getMainPlayerUsername(), color);

    }

    /**
     * Places the <code>chosenObjective</code> from one of the two available.
     *
     * @param chosenObjective the chosen objective.
     * @throws SuspendedGameException    if the game is suspended.
     * @throws RemoteException           in the event of an error occurring during the execution of a remote method.
     * @throws InvalidGamePhaseException if the game phase doesn't allow to place the objective card.
     */
    @Override
    public void placeObjectiveCard(int chosenObjective) throws SuspendedGameException, RemoteException, InvalidGamePhaseException {
        if (!game.isGameActive()) {
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if (game.getCurrentPhase() != GamePhase.Setup) {
            throw new InvalidGamePhaseException("You can only choose your private objective during the setup phase");
        }

        server.placeObjectiveCard(getMainPlayerUsername(), chosenObjective);
    }

    /**
     * Sends message from the <code>mainPlayerUsername</code>.
     *
     * @param message sent by the <code>mainPlayerUsername</code>.
     * @throws InvalidMessageException if the author doesn't match the author or the recipient doesn't exist
     * @throws RemoteException         in the event of an error occurring during the execution of a remote method.
     */
    @Override
    public void sendMessage(Message message) throws InvalidMessageException, RemoteException {

        if (!message.getSender().equals(getMainPlayerUsername())) {
            throw new InvalidMessageException("sender doesn't match the author's username");
        }
        if (!message.getRecipient().equals("Everyone") && game.getPlayer(message.getRecipient()) == null) {
            throw new InvalidMessageException("recipient doesn't exists");
        }

        server.sendMessage(message);
    }

    @Override
    public void setPlayersNumber(int playersNumber) throws RemoteException, InvalidPlayersNumberException {
        if (playersNumber > 4 || playersNumber < 2) {
            throw new InvalidPlayersNumberException();
        }
        server.setPlayersNumber(mainPlayerUsername, playersNumber);
    }

    /**
     * Disconnects the <code>username</code> from the game.
     *
     * @param username of the player.
     * @throws RemoteException in the event of an error occurring during the execution of a remote method.
     */
    @Override
    public void disconnect(String username) throws RemoteException {
        server.disconnect(username);
    }

    /**
     * Updates the current game to <code>clientGame</code>.
     *
     * @param clientGame to which the game is to be updated.
     */
    public void updateAfterConnection(ClientGame clientGame) {
        game = clientGame;
    }

    /**
     * Updates the player's status and if the player isn't found, it adds it and updates its status.
     *
     * @param isConnected player status.
     * @param username    of the player.
     */
    public void updatePlayerStatus(boolean isConnected, String username) {
        if (!game.containsPlayer(username)) {
            game.addPlayer(username);
        }
        game.getPlayer(username).setNetworkStatus(isConnected);
    }

    /**
     * Updates the lobby with the <code>usernames</code> of the players.
     *
     * @param usernames of the players.
     */
    public void updatePlayersInLobby(List<String> usernames) {
        game = new ClientGame(usernames);
    }

    /**
     * Updates the <code>color</code> of the <code>username</code>.
     *
     * @param color    chosen by the player.
     * @param username of the player.
     */
    public void updateColor(PlayerColor color, String username) {
        game.getPlayer(username).setColor(color);
    }

    /**
     * Updates the <code>chosenObjective</code> of the <code>username</code>.
     *
     * @param chosenObjective the chosen objective.
     * @param username        of the player.
     */
    public void updateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        game.getPlayer(username).updateObjectiveCard(chosenObjective);
    }

    /**
     * Updates the playground and the player's hand after a placement.
     *
     * @param positionToCornerCovered a map with the covered corners.
     * @param newAvailablePositions   a list with the available positions.
     * @param newResources            a map with the new acquired resources.
     * @param points                  present after the placement.
     * @param username                of the player.
     * @param placedCard              the card that has been placed.
     * @param placedSide              the side of the card that has been placed.
     * @param position                of the card in the playground.
     */
    public void updateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) {

        ClientPlayground playground = game.getPlaygroundByUsername(username);

        playground.setPoints(points);
        playground.addNewAvailablePositions(newAvailablePositions);
        playground.setCoveredCorner(positionToCornerCovered);
        playground.placeTile(position, new ClientTile(placedCard.getFace(placedSide)));
        playground.updateResources(newResources);

        game.getPlayer(username).removePlayerCard(placedCard);

        if (!getGamePhase().equals(GamePhase.Setup) && this.getMainPlayerUsername().equals(username)) {
            this.game.setCurrentPhase(GamePhase.DrawNormal);
        }
    }

    /**
     * Updates the player's hand and the golden/resource deck or face up card after a draw.
     *
     * @param drawnCard      the card that has been drawn.
     * @param newTopBackDeck the new card that replaces the previous card that was present in the deck.
     * @param newFaceUpCard  the new face up card that replaces the previous one.
     * @param username       of the player who performs the drawing
     * @param boardPosition  from which the card was selected, 4 for golden deck, 5 for resource deck and 0,1,2 or 3 for face up cards.
     */
    public void updateAfterDraw(ClientCard drawnCard, ClientFace newTopBackDeck, ClientCard newFaceUpCard, String username, int boardPosition) {
        assert (boardPosition <= 5 && boardPosition >= 0);

        game.getPlayer(username).addPlayerCard(drawnCard);

        if (boardPosition < 4) {
            game.getClientBoard().replaceFaceUpCard(newFaceUpCard, boardPosition);
            if (boardPosition <= 1) {
                game.getClientBoard().setResourceDeckTopBack(newTopBackDeck);
            } else {
                game.getClientBoard().setGoldenDeckTopBack(newTopBackDeck);
            }
        } else if (boardPosition == 4) {
            game.getClientBoard().setGoldenDeckTopBack(newTopBackDeck);
        } else {
            game.getClientBoard().setResourceDeckTopBack(newTopBackDeck);
        }

    }

    /**
     * Updates the chat with a new <code>message</code>.
     *
     * @param message the new message to be added to the chat.
     */
    //todo check if other players discard private message of others or if they save them but the view avoid the to show to the players
    public void updateChat(Message message) {
        game.getMessages().add(message);
    }

    /**
     * Updates the current player and its <code>phase</code>.
     *
     * @param currentPlayerIdx index of the current player
     * @param phase            in which the current player is.
     */
    public void updateCurrentPlayer(int currentPlayerIdx, GamePhase phase) {
        game.setCurrentPlayerIdx(currentPlayerIdx);
        game.setCurrentPhase(phase);
    }

    /**
     * Updates the game from an active state to an inactive state
     */
    public void updateSuspendedGame() {
        game.setGameActive(!game.isGameActive());
    }

    /**
     * Checks if the given <code>position</code> is available or not.
     *
     * @param position to be verified.
     * @return true if the <code>position</code> is available, false otherwise.
     */
    private boolean checkPosition(Position position) {
        return getMainPlayerPlayground().getAvailablePositions().contains(position);
    }

    /**
     * Checks if the playground satisfies face's requirements.
     *
     * @param card the card containing the requirements to be checked.
     * @return true if the playground contains enough resources to place the card.
     */
    private boolean checkRequirements(ClientCard card) {
        for (Symbol s : card.getFront().getRequirements().keySet()) {
            if (getMainPlayer().getAmountResource(s) < card.getFront().getRequirements().get(s)) {
                return false;
            }
        }
        return true;
    }

    public ClientPlayer getMainPlayer() {
        return game.getPlayer(mainPlayerUsername);
    }

    public ClientCard getMainPlayerCard(int cardHandPosition) {
        return getMainPlayer().getPlayerCard(cardHandPosition);
    }

    public List<ClientCard> getMainPlayerCards() {
        return getMainPlayer().getPlayerCards();
    }

    public ClientCard getMainPlayerStarter() {
        return getMainPlayer().getStarterCard();
    }

    public String getMainPlayerUsername() {
        return mainPlayerUsername;
    }

    public ClientPlayground getMainPlayerPlayground() {
        return getMainPlayer().getPlayground();
    }

    public boolean isMainPlayerTurn() {
        return getMainPlayer().getUsername().equals(game.getCurrentPlayer().getUsername());
    }

    public GamePhase getGamePhase() {
        return this.game.getCurrentPhase();
    }

    public String getLastMessage() {
        Message lastMessage = game.getMessages().getLast();
        return lastMessage.getSender() + " -> " + lastMessage.getRecipient() + ": " + lastMessage.getContent();
    }

    public List<Message> getMessage() {
        return game.getMessages();
    }

    public PlayerColor getMainColor() {
        return game.getPlayer(mainPlayerUsername).getColor();
    }

    public boolean getPlayerStatus() {
        return game.getPlayer(mainPlayerUsername).isConnected();
    }

    public List<String> getConnectedUsernames() {
        return this.game.getPlayers().stream().filter(ClientPlayer::isConnected).map(ClientPlayer::getUsername).toList();
    }

    public List<String> getUsernames() {
        return this.game.getPlayers().stream().map(ClientPlayer::getUsername).toList();
    }

    public List<ClientPlayer> getPlayers() {
        return this.game.getPlayers();
    }

    public List<ClientCard> getFaceUpCards() {
        return this.game.getClientBoard().getFaceUpCards();
    } //review

    public ClientFace getGoldenDeckTopBack() {
        return this.game.getClientBoard().getGoldenDeckTopBack();
    } //review

    public ClientFace getResourceDeckTopBack() {
        return this.game.getClientBoard().getResourceDeckTopBack();
    }

    public String getCurrentPlayerUsername() {
        return game.getPlayer(game.getCurrentPlayerIdx()).getUsername();
    }

    public List<ClientObjectiveCard> getObjectiveCards() {
        return this.game.getClientBoard().getCommonObjectives();
    }

    public ClientPlayground getPlaygroundByUsername(String username){
        return game.getPlaygroundByUsername(username);
    }

    public ClientPlayer getPlayer(String username) {
        return game.getPlayer(username);
    }

    public boolean isGameActive() {
        return game.isGameActive();
    }

    public boolean isMainPlaygroundEmpty() {
        return getMainPlayerPlayground().getArea().values().stream()
                .noneMatch(tile -> tile.sameAvailability(Availability.OCCUPIED));
    }

    public List<ClientObjectiveCard> getPlayerObjectives() {
        return getMainPlayer().getObjectiveCards();
    }
}
