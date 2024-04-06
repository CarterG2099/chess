package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGame extends ServerMessage {
    GameData gameData;

    public LoadGame(GameData gameData) {
        super(ServerMessageType.LOAD_GAME);
        this.gameData = gameData;
    }

    public GameData gameData() {
        return gameData;
    }

    public ChessGame game() {
        return gameData().chessGame();
    }
}
