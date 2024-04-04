package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    int gameID;
    String playerColor;
    public Leave(String authToken, int gameID, String playerColor) {
        super(authToken, CommandType.LEAVE);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
}
