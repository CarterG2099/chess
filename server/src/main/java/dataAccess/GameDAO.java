package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void deleteGameData() throws DataAccessException;
    ArrayList<GameData> getGameList();

    void addGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameId);

    void deleteGame(GameData game);
}
