package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.color.PlayerColor;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamephase.GamePhase;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.network.client.model.card.ClientFace;

import java.util.List;
import java.util.Map;

public interface GameListener {
    /**
     * Method used to show the update from the current game to <code>clientGame</code>.
     *
     * @param clientGame the clientGame to which the game is to be updated.
     */
    void updateAfterConnection(ClientGame clientGame);

    /**
     * Method used to update the lobby with the <code>usernames</code> of the players.
     *
     * @param usernames of the players.
     */
    void showUpdatePlayersInLobby(List<String> usernames);

    void showUpdateExceedingPlayer();

    /**
     * Method used to show basic player information.
     * It notifies about the player disconnections, and also when a new player connects.
     *
     * @param isConnected player status.
     * @param username    of the player.
     */
    void showUpdatePlayerStatus(boolean isConnected, String username);

    /**
     * Method used to show the chosen color.
     * All clients receive the information, the ones that aren't the <code>username</code>, will remove
     * <code>color</code> from their list of available colors.
     *
     * @param color    the color chosen by the player.
     * @param username the username of the player.
     */
    void showUpdateColor(PlayerColor color, String username);

    /**
     * Method used to show the objective card.
     *
     * @param chosenObjective the chosen objective.
     * @param username        of the player.
     */
    void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username);

    /**
     * Method used to show updated information after a place.
     * The two list positions and tiles represent every new available tile added after the last place invocation
     * symbols and total amount represents the new resources added after the last place invocation
     * consider both couples like maps.
     *
     * @param positionToCornerCovered a map with the covered corners.
     * @param newAvailablePositions   a list with every new available tile added after the last placement.
     * @param newResources            the new resources added after the last placement.
     * @param points                  the points present after the placement.
     * @param username                the username of the player.
     * @param placedCard              the card that has been placed.
     * @param placedSide              the side of the card that has been placed.
     * @param position                the position of the card in the playground.
     */
    void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position);

    /**
     * Method used to notify the update after a draw.
     *
     * @param drawnCard     the drawn card.
     * @param newTopDeck    the new card that replaces the previous card that was present in the deck.
     * @param newFaceUpCard the new face up card that replaces the previous one.
     * @param username      the username of the player who performs the drawing.
     * @param boardPosition from which the card was selected, 4 for golden deck, 5 for resource deck and 0,1,2 or 3 for
     *                      face up cards.
     */
    void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition);

    /**
     * Method used to show the updated chat.
     *
     * @param message the new message to be displayed.
     */
    void showUpdateChat(Message message);

    /**
     * Method used to show the current player's turn.
     *
     * @param currentPlayerIdx the index of the current player.
     * @param phase            the phase in which the current player is.
     */
    void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase);

    /**
     * Method used to show the update of the state of the game.
     */
    void showUpdateGameState();

    /**
     * Method used to show the winners of the game.
     *
     * @param winners the winners of the game.
     */
    void showWinners(List<String> winners);

    /**
     * Method used to report error <code>details</code>.
     *
     * @param details the details to be reported.
     */
    void reportError(String details);


    /**
     * Method used to connect the player or an output with the <code>details</code>, depending on whether the
     * player is <code>accepted</code> or not.
     *
     * @param accepted boolean indicating whether the player is accepted or not.
     * @param details  the error details.
     */
    void resultOfLogin(boolean accepted, String details);
}
