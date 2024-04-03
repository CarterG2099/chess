package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {
    int gameID;
    CommandType commandType = CommandType.JOIN_OBSERVER;
    public JoinObserver(String authToken) {
        super(authToken, CommandType.LEAVE);
    }
}
