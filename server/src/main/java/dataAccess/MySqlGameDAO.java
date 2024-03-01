package dataAccess;

import model.GameData;

import java.util.ArrayList;

public class MySqlGameDAO implements GameDAO{
    @Override
    public void deleteGameData() throws DataAccessException {

    }

    @Override
    public ArrayList<GameData> getGameList() {
        return null;
    }

    @Override
    public void addGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {

    }
}
