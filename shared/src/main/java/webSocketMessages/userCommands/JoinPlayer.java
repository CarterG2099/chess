package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    int gameID;
    ChessGame.TeamColor playerColor;
    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken, CommandType.JOIN_PLAYER);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public int gameID() {
        return gameID;
    }

    public ChessGame.TeamColor playerColor() {
        return playerColor;
    }
}
