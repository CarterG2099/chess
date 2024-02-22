package dataAccess;

import model.GameData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

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

    public ArrayList<GameData> getGameList(){
        return gameDataArrayList;
    }

    public void addGame(GameData game){
        gameDataArrayList.add(game);
    }
}
