package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    int gameID;
    ChessGame.TeamColor playerColor;
    CommandType commandType = CommandType.JOIN_PLAYER;
    public JoinPlayer(String authToken) {
        super(authToken, CommandType.LEAVE);
    }
}
