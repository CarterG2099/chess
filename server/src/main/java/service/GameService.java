package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;
import server.Server;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class GameService {

    public ArrayList<GameData> getGames(){
        return Server.gameDAO.getGameList();
    }

    public GameData createGame(GameData gameData) throws DataAccessException {
        if (gameData.gameName().isEmpty()) {
            throw new DataAccessException("Bad Request", 400);
        }
        String gameId = String.valueOf(new Random().nextInt(1000));
        GameData newGame = new GameData(gameId, "", "", gameData.gameName(), new ChessGame());
        Server.gameDAO.addGame(newGame);
        return newGame;
    }
}
