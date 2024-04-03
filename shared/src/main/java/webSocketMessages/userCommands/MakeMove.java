package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    int gameID;
    ChessMove move;
    public MakeMove(String authToken, int gameID, ChessMove move) {
        super(authToken, CommandType.MAKE_MOVE);
        this.gameID = gameID;
        this.move = move;
    }
}
