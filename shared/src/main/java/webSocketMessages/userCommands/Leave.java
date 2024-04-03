package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    int gameID;
    public Leave(String authToken, int gameID) {
        super(authToken, CommandType.LEAVE);
        this.gameID = gameID;
    }
}
