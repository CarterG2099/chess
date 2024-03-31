package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    ChessGame game;
    public LoadGame(ServerMessageType type) {
        super(type);
        type = ServerMessageType.LOAD_GAME;
    }
}
