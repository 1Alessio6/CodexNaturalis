package it.polimi.ingsw.network.client.controller;

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
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.board.ClientTile;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientController implements ClientActions {

    private VirtualServer server;
    private ClientGame game;

    private String mainPlayerUsername=""; // todo. set by the view after user's input

    public ClientController(VirtualServer server){
        this.server = server;
    }

    @Override
    public void connect(VirtualView client, String username) throws InvalidUsernameException, RemoteException, FullLobbyException {
        server.connect(client,username);
    }

    @Override
    public void placeCard(int cardHandPosition, Side selectedSide, Position position) throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, RemoteException {

        if (!game.isGameActive()) {
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if(!isMainPlayerTurn()){
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

        try {
            server.placeCard(getMainPlayerUsername(), getMainPlayerCard(cardHandPosition).getBackId(), getMainPlayerCard(cardHandPosition).getFrontId(), selectedSide, position);
        } catch (Playground.UnavailablePositionException | Playground.NotEnoughResourcesException e) {
            System.err.println("Error check methods should have avoid an incorrect move");
        } catch (InvalidPlayerActionException | SuspendedGameException | InvalidGamePhaseException e) {
            System.err.println("Error");
        } catch (InvalidCardIdException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void draw(int IdToDraw) throws InvalidGamePhaseException, EmptyDeckException, NotExistingFaceUp, SuspendedGameException, RemoteException, InvalidIdForDrawingException {

        if(IdToDraw > 5 || IdToDraw < 0){
            throw new InvalidIdForDrawingException();
        }

        if (!game.isGameActive()) {
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if(!isMainPlayerTurn()){
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
            case 1:
                if (game.isFaceUpSlotEmpty(1)) {
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
            case 2:
                if (game.isFaceUpSlotEmpty(2)) {
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
            case 3:
                if (game.isFaceUpSlotEmpty(3)) {
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
            case 4:
                if (game.getClientBoard().isGoldenDeckEmpty()) {
                    throw new EmptyDeckException("You chose to draw from an empty deck");
                }
            case 5:
                if (game.getClientBoard().isResourceDeckEmpty()) {
                    throw new EmptyDeckException("You chose to draw from an empty deck");
                }
            default:
                System.err.println("ID not valid");
        }

        try {
            server.draw(getMainPlayerUsername(), IdToDraw);
        } catch (EmptyDeckException | InvalidGamePhaseException | InvalidPlayerActionException | InvalidIdForDrawingException | InvalidFaceUpCardException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void placeStarter(Side side) throws SuspendedGameException, RemoteException, InvalidGamePhaseException {

        if (!game.isGameActive()) {
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if (game.getCurrentPhase() != GamePhase.Setup) {
            throw new InvalidGamePhaseException("You can only place your starter card during the setup phase");
        }

        try {
            server.placeStarter(getMainPlayerUsername(), side);
        } catch (InvalidPlayerActionException | InvalidGamePhaseException e) {
            System.err.println(e.getMessage());
        }
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

        try {
            server.chooseColor(getMainPlayerUsername(), color);
        } catch (InvalidPlayerActionException | InvalidGamePhaseException | NonexistentPlayerException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void placeObjectiveCard(int chosenObjective) throws SuspendedGameException, RemoteException, InvalidGamePhaseException {
        if (!game.isGameActive()) {
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if (game.getCurrentPhase() != GamePhase.Setup) {
            throw new InvalidGamePhaseException("You can only choose your private objective during the setup phase");
        }

        try {
            server.placeObjectiveCard(getMainPlayerUsername(), chosenObjective);
        } catch (InvalidGamePhaseException | InvalidPlayerActionException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void sendMessage(Message message) throws InvalidMessageException, RemoteException {

        if (!message.getSender().equals(getMainPlayerUsername())) {
            throw new InvalidMessageException("sender doesn't match the author's username");
        }
        if (game.getPlayer(message.getRecipient()) == null) {
            throw new InvalidMessageException("recipient doesn't exists");
        }

        server.sendMessage(message);
    }

    @Override
    public void setPlayersNumber(int playersNumber) throws RemoteException, InvalidPlayersNumberException {
        if(playersNumber > 4 || playersNumber < 2){
            throw new InvalidPlayersNumberException();
        }
        server.setPlayersNumber(playersNumber);
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

    public void updatePlayersInLobby(List<String> usernames){
        game = new ClientGame(usernames);
    }

    public void updateColor(PlayerColor color, String username) {
        game.getPlayer(username).setColor(color);
    }

    public void updateObjectiveCard(ClientCard chosenObjective, String username) {
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

    }

    public void updateAfterDraw(ClientCard drawnCard, ClientFace newTopBackDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {
        assert (boardPosition <= 5 && boardPosition >= 0);

        game.getPlayer(username).addPlayerCard(drawnCard);

        if (boardPosition < 4) {
            game.getClientBoard().addFaceUpCards(newFaceUpCard, boardPosition);
            if(boardPosition < 1){
                game.getClientBoard().setResourceDeckTopBack(newTopBackDeck);
            }
            else{
                game.getClientBoard().setGoldenDeckTopBack(newTopBackDeck);
            }
        }
        else if(boardPosition == 4){
            game.getClientBoard().setGoldenDeckTopBack(newTopBackDeck);
        }
        else{
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
        for (Symbol s : card.getRequiredResources().keySet()) {
            if (getMainPlayer().getAmountResource(s) < card.getRequiredResources().get(s)) {
                return false;
            }
        }
        return true;
    }

    public ClientPlayer getMainPlayer(){
        return game.getPlayer(mainPlayerUsername);
    }

    public ClientCard getMainPlayerCard(int cardHandPosition){
        return getMainPlayer().getPlayerCard(cardHandPosition);
    }

    public String getMainPlayerUsername(){
        return mainPlayerUsername;
    }

    public ClientPlayground getMainPlayerPlayground(){
        return getMainPlayer().getPlayground();
    }

    public boolean isMainPlayerTurn(){
        return getMainPlayer().getUsername().equals(game.getCurrentPlayer().getUsername());
    }

}
