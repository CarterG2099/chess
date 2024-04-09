package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGame extends ServerMessage {
    GameData gameData;
    ChessGame game;
    public LoadGame(GameData gameData) {
        super(ServerMessageType.LOAD_GAME);
        this.gameData = gameData;
        this.game = gameData.chessGame();
    }

    public GameData gameData() {
        return gameData;
    }

    public String playerColor() {
        return gameData().playerColor();
    }
}
