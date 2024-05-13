package it.polimi.ingsw.network.server.socket.message;

public enum ServerType {
    CONNECT,
    PLACE_STARTER,
    CHOOSE_COLOR,
    PLACE_OBJECTIVE,
    PLACE_CARD,
    DRAW,
    SEND_CHAT_MESSAGE,
    SET_PLAYER_NUMBER,
    DISCONNECT,
    SEND_PING
}
