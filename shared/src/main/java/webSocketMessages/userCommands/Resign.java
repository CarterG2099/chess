package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    int gameID;

    public Resign(String authToken, int gameID) {
        super(authToken, CommandType.RESIGN);
        this.gameID = gameID;
    }
}
