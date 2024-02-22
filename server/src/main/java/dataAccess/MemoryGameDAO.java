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

    public void addGame(GameData gameToAdd) throws DataAccessException{
        for(GameData game : gameDataArrayList){
            if(game.gameId() == gameToAdd.gameId()){
                throw new DataAccessException("Already taken", 403);
            }
            else {
                gameDataArrayList.add(gameToAdd);
            }
        }
    }

    public GameData getGame(int gameId){
        for(GameData game : gameDataArrayList){
            if(game.gameId() == gameId){
                return game;
            }
        }
        return null;
    }

    public void deleteGame(GameData gameToDelete){
        gameDataArrayList.removeIf(game -> gameToDelete.equals(game));
    }
}
