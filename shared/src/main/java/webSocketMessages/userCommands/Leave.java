package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    int gameID;
    CommandType commandType = CommandType.LEAVE;
    public Leave(String authToken) {
        super(authToken);
    }
}
