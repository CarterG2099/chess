package webSocketMessages.userCommands;

import chess.ChessGame;
import model.GameData;

public class JoinPlayer extends UserGameCommand {
    int gameID;
    ChessGame.TeamColor playerColor;

    GameData gameData;

    public JoinPlayer(String authToken, GameData gameData) {
        super(authToken, CommandType.JOIN_PLAYER);
        this.gameID = gameData.gameID();
        this.playerColor = ChessGame.TeamColor.valueOf(String.valueOf(gameData.playerColor()));
        this.gameData = gameData;
    }

    public int gameID() {
        return gameID;
    }

    public ChessGame.TeamColor playerColor() {
        return playerColor;
    }

    public GameData gameData() {
        return gameData;
    }
}
