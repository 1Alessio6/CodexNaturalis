package it.polimi.ingsw.network;

/**
 * Enumeration representing the means by which the client interacts with the server and conversely.
 */
public enum Type {
    /**
     * HEARTBEAT refers to the constant pings sent by the client.
     */
    HEARTBEAT,
    // from client
    /**
     * UPDATE_CREATOR refers to the action of updating the view when the first player enters.
     */
    UPDATE_CREATOR,
    /**
     * UPDATE_AFTER_LOBBY_CRASH refers to the action of showing the crash of the lobby in the view.
     */
    UPDATE_AFTER_LOBBY_CRASH,
    /**
     * UPDATE_AFTER_CONNECTION refers to the action of showing the playing area after the connection of the number of
     * players specified in the lobby.
     */
    UPDATE_AFTER_CONNECTION,
    /**
     * SHOW_UPDATE_PLAYERS_IN_LOBBY refers to the action of introducing new players to the lobby.
     */
    SHOW_UPDATE_PLAYERS_IN_LOBBY,
    /**
     * SHOW_UPDATE_PLAYER_STATUS refers to the action of displaying the current status of a player, that is, connected
     * or disconnected.
     */
    SHOW_UPDATE_PLAYER_STATUS,
    /**
     * SHOW_UPDATE_COLOR refers to the action of showing the updated color of the player.
     */
    SHOW_UPDATE_COLOR,
    /**
     * SHOW_UPDATE_OBJECTIVE_CARD refers to the action of showing the objective card chosen by the player.
     */
    SHOW_UPDATE_OBJECTIVE_CARD,
    /**
     * SHOW_UPDATE_AFTER_PLACE refers to the action of showing the updated playground and player's hand after a
     * placement.
     */
    SHOW_UPDATE_AFTER_PLACE,
    /**
     * SHOW_UPDATE_AFTER_DRAW refers to the action of showing the updated player's hand, face up cards or golden/resource
     * deck after a draw.
     */
    SHOW_UPDATE_AFTER_DRAW,
    /**
     * SHOW_UPDATE_CHAT refers to the action of updating the chat after the arrival of a new message.
     */
    SHOW_UPDATE_CHAT,
    /**
     * SHOW_UPDATE_CURRENT_PLAYER refers to the action of updating the current player and its game phase.
     */
    SHOW_UPDATE_CURRENT_PLAYER,
    /**
     * SHOW_UPDATE_SUSPENDED_GAME refers to the action of updating the game state to suspended.
     */
    SHOW_UPDATE_SUSPENDED_GAME,
    /**
     * SHOW_WINNERS refers to the action of showing the game winners.
     */
    SHOW_WINNERS,
    /**
     * ERROR refers to the action of showing an error.
     */
    ERROR,

    // from server
    FROM_SERVER,
    /**
     * CONNECT refers to the action of connecting a player to the game.
     */
    CONNECT,
    /**
     * PLACE_STARTER refers to the action of placing the starter card.
     */
    PLACE_STARTER,
    /**
     * CHOOSE_COLOR refers to the action of assigning a specific color to a player.
     */
    CHOOSE_COLOR,
    /**
     * PLACE_OBJECTIVE refers to the action of placing the chosen objective card.
     */
    PLACE_OBJECTIVE,
    /**
     * PLACE_CARD refers to the action of placing a certain card in the playground.
     */
    PLACE_CARD,
    /**
     * DRAW refers to the action of drawing a card from the face up cards or the golden/resource deck.
     */
    DRAW,
    /**
     * SEND_CHAT_MESSAGE refers to the action of sending a new message in chat.
     */
    SEND_CHAT_MESSAGE,
    /**
     * SET_PLAYER_NUMBER refers to the action of setting the number of players in the game.
     */
    SET_PLAYER_NUMBER,
    /**
     * DISCONNECT refers to the action of disconnecting a player from the game.
     */
    DISCONNECT,
    /**
     * RESULT_OF_LOGIN refer to the action of showing the result of the login, that can be <code>accepted</code> or not.
     */
    RESULT_OF_LOGIN,
    /**
     * FULL_LOBBY refers to the action of showing the fullness of the lobby.
     */
    FULL_LOBBY,
    /**
     * EXCEEDING_PLAYER refers to the action of notifying an exceeding player about his/her status.
     */
    EXCEEDING_PLAYER,
    GAME_ALREADY_STARTED
}
