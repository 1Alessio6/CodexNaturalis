package it.polimi.ingsw.network.client.controller;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.ClientPhase;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientController implements ClientActions {

    private VirtualServer server;
    private ClientGame game;

    @Override
    public void connect(VirtualView client, String username) throws InvalidUsernameException, RemoteException {

    }

    @Override
    public void placeCard(int cardHandPosition, Side selectedSide, Position position) throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, RuntimeException {

        if (!game.isGameActive()){
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if(game.getCurrentPhase() != ClientPhase.NORMAL_PLACE && game.getCurrentPhase() != ClientPhase.ADDITIONAL_PLACE){
            throw new InvalidGamePhaseException("You can't place now");
        }

        if (selectedSide == Side.FRONT) {
            if (!checkRequirements(this.game.getMainPlayerCard(cardHandPosition))) {
                throw new Playground.NotEnoughResourcesException("You don't have the resources to place this side of the card");
            }
        }

        if (!checkPosition(position)) {
            throw new Playground.UnavailablePositionException("The position insert isn't available");
        }

        try{
            server.placeCard(this.game.getMainPlayerUsername(), this.game.getMainPlayerCard(cardHandPosition).getBackId(), this.game.getMainPlayerCard(cardHandPosition).getFrontId(), selectedSide, position);
        } catch (Playground.UnavailablePositionException | Playground.NotEnoughResourcesException e) {
            System.err.println("Error check methods should have avoid an incorrect move");
        } catch (InvalidPlayerActionException | SuspendedGameException | InvalidGamePhaseException e) {
            System.err.println("Error");
        }
    }

    @Override
    public void draw(int IdToDraw) throws InvalidGamePhaseException, EmptyDeckException, NotExistingFaceUp, SuspendedGameException, RemoteException {

        if (!game.isGameActive()){
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if(game.getCurrentPhase() != ClientPhase.NORMAL_DRAW){
            throw new InvalidGamePhaseException("You can't draw now");
        }

        switch(IdToDraw){
            case 0:
                if(game.isFaceUpSlotEmpty(0)){
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
            case 1:
                if(game.isFaceUpSlotEmpty(1)){
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
            case 2:
                if(game.isFaceUpSlotEmpty(2)){
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
            case 3:
                if(game.isFaceUpSlotEmpty(3)){
                    throw new NotExistingFaceUp("This face up slot is empty");
                }
            case 4:
                if(game.getClientBoard().isGoldenDeckEmpty()){
                    throw new EmptyDeckException("You chose to draw from an empty deck");
                }
            case 5:
                if(game.getClientBoard().isResourceDeckEmpty()){
                    throw new EmptyDeckException("You chose to draw from an empty deck");
                }
            default:
                System.err.println("ID not valid");
        }

        try{
            server.draw(game.getMainPlayerUsername(), IdToDraw);
        }catch(EmptyDeckException | InvalidGamePhaseException | InvalidPlayerActionException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void placeStarter(Side side) throws SuspendedGameException, RemoteException, InvalidGamePhaseException {
        if (!game.isGameActive()){
            throw new SuspendedGameException("The game is suspended, you can only text messages");
        }

        if(game.getCurrentPhase() != ClientPhase.SETUP){
            throw new InvalidGamePhaseException("You can only place your starter card during the setup phase");
        }

        try{
            server.placeStarter(game.getMainPlayerUsername(), side);
        }catch(InvalidPlayerActionException | InvalidGamePhaseException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void chooseColor(PlayerColor color) throws InvalidColorException, SuspendedGameException {

    }

    @Override
    public void placeObjectiveCard(int chosenObjective) throws SuspendedGameException, RemoteException, InvalidGamePhaseException {

    }

    @Override
    public void sendMessage(Message message) throws InvalidMessageException, RemoteException {

    }

    @Override
    public void setPlayersNumber(int playersNumber) throws RemoteException {

    }

    public void updateCreator() throws RemoteException {

    }

    public void updateAfterLobbyCrash() {

    }

    public void updateAfterConnection(ClientGame clientGame) {

    }

    public void updatePlayerStatus(boolean isConnected, String username) {

    }

    public void updateInitialPlayerStatus(ClientPlayer player) {

    }

    public void updateBoardSetUp(int[] commonObjectiveID, int topBackID, int topGoldenBackID, int[] faceUpCards) {

    }

    public void updateStarterPlacement(String username, int faceId) {

    }

    public void updateColor(PlayerColor color, String username) {

    }

    public void updateObjectiveCard(ClientCard chosenObjective, String username) {

    }

    void updateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Position position) {

    }

    void updateAfterDraw(ClientCard drawnCard, boolean isEmpty, ClientCard newTopDeck, ClientCard newFaceUpCard, ClientCard newTopCard, boolean additionalTurn, String username, int boardPosition) throws RemoteException {

    }

    //method to notify update after a draw

    void updateChat(Message message) {

    }

    void updateCurrentPlayer(ClientPlayer currentPlayer, ClientPhase phase) {

    }

    void updateSuspendedGame() {

    }

    void showWinners(List<String> winners) {
    }

    private boolean checkPosition(Position position) {
        return game.getMainPlayerPlayground().getAvailablePositions().contains(position);
    }

    private boolean checkRequirements(ClientCard card) {
        for (Symbol s : card.getRequiredResources().keySet()) {
            if (this.game.getMainPlayer().getAmountResource(s) < card.getRequiredResources().get(s)) {
                return false;
            }
        }
        return true;
    }

}
