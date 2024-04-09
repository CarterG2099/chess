package webSocketMessages.userCommands;

import model.GameData;

public class JoinPlayer extends UserGameCommand {
    int gameID;
    String playerColor;

    GameData gameData;

    public JoinPlayer(String authToken, GameData gameData) {
        super(authToken, CommandType.JOIN_PLAYER);
        this.gameID = gameData.gameID();
        this.playerColor = gameData.playerColor();
        this.gameData = gameData;
    }

    public int gameID() {
        return gameID;
    }

    public String playerColor() {
        return playerColor;
    }

    public GameData gameData() {
        return gameData;
    }
}
