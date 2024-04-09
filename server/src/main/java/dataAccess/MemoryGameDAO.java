package dataAccess;

import DataAccessException.DataAccessException;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    private final ArrayList<GameData> gameDataArrayList = new ArrayList<>();

    @Override
    public void deleteGameData() throws DataAccessException {
        try {
            gameDataArrayList.clear();
        } catch (Exception ex) {
            throw new DataAccessException("GDAO:deleteGameData", 500);
        }
    }

    public ArrayList<GameData> getGameList() {
        return gameDataArrayList;
    }

    public void addGame(GameData gameToAdd) throws DataAccessException {
        for (GameData game : gameDataArrayList) {
            if (game.gameID() == gameToAdd.gameID()) {
                throw new DataAccessException("Already taken in GDAO:addGame", 403);
            }
        }
        gameDataArrayList.add(gameToAdd);
    }

    public GameData getGame(int gameId) throws DataAccessException {
        for (GameData game : gameDataArrayList) {
            if (game.gameID() == gameId) {
                return game;
            }
        }
        throw new DataAccessException("Bad Request in GDAO:getGame", 400);
    }

    public void deleteGame(GameData gameToDelete) throws DataAccessException {
        for (GameData game : gameDataArrayList) {
            if (game.gameID() == gameToDelete.gameID()) {
                gameDataArrayList.remove(game);
                return;
            }
        }
        throw new DataAccessException("Bad Request in GDAO:deleteGame", 400);
    }
}
