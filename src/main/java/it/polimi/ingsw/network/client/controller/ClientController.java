package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.controller.InvalidIdForDrawingException;
import it.polimi.ingsw.model.InvalidGamePhaseException;
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

public class ClientController implements ClientActions {

    private final VirtualServer server;
    private ClientGame game;

    private String mainPlayerUsername = ""; // todo. set by the view after user's input

    public ClientController(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void connect(VirtualView client, String username) throws InvalidUsernameException, RemoteException, FullLobbyException {
        server.connect(client, username);
        this.mainPlayerUsername = username;
    }

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

    @Override
    public void disconnect(String username) throws RemoteException {
        server.disconnect(username);
    }

    public void updateAfterConnection(ClientGame clientGame) {
        game = clientGame;
    }

    public void updatePlayerStatus(boolean isConnected, String username) {
        if (!game.containsPlayer(username)) {
            game.addPlayer(username);
        }
        game.getPlayer(username).setNetworkStatus(isConnected);
    }

    public void updatePlayersInLobby(List<String> usernames) {
        game = new ClientGame(usernames);
    }

    public void updateColor(PlayerColor color, String username) {
        game.getPlayer(username).setColor(color);
    }

    public void updateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        game.getPlayer(username).updateObjectiveCard(chosenObjective);
    }

    public void updateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) {

        ClientPlayground playground = game.getPlaygroundByUsername(username);

        playground.setPoints(points);
        playground.addNewAvailablePositions(newAvailablePositions);
        playground.setCoveredCorner(positionToCornerCovered);
        playground.placeTile(position, new ClientTile(placedCard.getFace(placedSide)));
        playground.updateResources(newResources);

        game.getPlayer(username).removePlayerCard(placedCard);

        if(!getGamePhase().equals(GamePhase.Setup) && this.getMainPlayerUsername().equals(username)) {
            this.game.setCurrentPhase(GamePhase.DrawNormal);
        }
    }

    public void updateAfterDraw(ClientCard drawnCard, ClientFace newTopBackDeck, ClientCard newFaceUpCard, String username, int boardPosition) {
        assert (boardPosition <= 5 && boardPosition >= 0);

        game.getPlayer(username).addPlayerCard(drawnCard);

        if (boardPosition < 4) {
            game.getClientBoard().replaceFaceUpCard(newFaceUpCard, boardPosition);
            if (boardPosition < 1) {
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

    //todo check if other players discard private message of others or if they save them but the view avoid the to show to the players
    public void updateChat(Message message) {
        game.getMessages().add(message);
    }

    public void updateCurrentPlayer(int currentPlayerIdx, GamePhase phase) {
        game.setCurrentPlayerIdx(currentPlayerIdx);
        game.setCurrentPhase(phase);
    }

    public void updateSuspendedGame() {
        game.setGameActive(!game.isGameActive());
    }

    private boolean checkPosition(Position position) {
        return getMainPlayerPlayground().getAvailablePositions().contains(position);
    }

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

    public List<Message> getMessage(){return game.getMessages();}

    public PlayerColor getColor() {
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

   public List<ClientObjectiveCard> getObjectiveCards(){return this.game.getClientBoard().getCommonObjectives();}
}
