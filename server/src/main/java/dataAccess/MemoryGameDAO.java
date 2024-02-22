package dataAccess;

import model.GameData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
    private final ArrayList<GameData> gameDataArrayList = new ArrayList<>();
    @Override
    public void deleteGameData() throws DataAccessException {
        try {
            gameDataArrayList.clear();
        } catch (Exception ex) {
            throw new DataAccessException("Game Data", 500);
        }
    }
}
