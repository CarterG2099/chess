package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {
    int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(authToken, CommandType.JOIN_OBSERVER);
        this.gameID = gameID;
    }

    public int gameID() {
        return gameID;
    }
}
