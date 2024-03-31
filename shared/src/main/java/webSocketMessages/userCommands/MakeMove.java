package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    int gameID;
    ChessMove move;
    CommandType commandType = CommandType.MAKE_MOVE;
    public MakeMove(String authToken) {
        super(authToken);
    }
}
