package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    int gameID;
    CommandType commandType = CommandType.RESIGN;
    public Resign(String authToken) {
        super(authToken, CommandType.LEAVE);
    }
}
