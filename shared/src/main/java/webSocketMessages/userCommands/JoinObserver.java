package webSocketMessages.userCommands;

import model.GameData;

public class JoinObserver extends UserGameCommand {
    int gameID;

    GameData gameData;

    public JoinObserver(String authToken, GameData gameData) {
        super(authToken, CommandType.JOIN_OBSERVER);
        this.gameID = gameData.gameID();
        this.gameData = gameData;
    }

    public int gameID() {
        return gameID;
    }

    public GameData gameData() {
        return gameData;
    }
}
