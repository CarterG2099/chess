package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;
import model.UserData;
import server.Server;

import java.util.ArrayList;
import java.util.Random;

public class GameService {

    public ArrayList<GameData> getGames() throws DataAccessException {
        return Server.gameDAO.getGameList();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return Server.gameDAO.getGame(gameID);
    }

    public GameData createGame(GameData gameData) throws DataAccessException {
        if (gameData.gameName() == null) {
            throw new DataAccessException("Bad Request in GS:createGame", 400);
        }
        int gameId = new Random().nextInt(1000);
        GameData newGame = new GameData(gameId, null, null, gameData.gameName(), new ChessGame(), "", new ArrayList<>());
        Server.gameDAO.addGame(newGame);
        return newGame;
    }

    public GameData joinGame(GameData gameData, UserData user) throws DataAccessException {
        GameData oldGame = Server.gameDAO.getGame(gameData.gameID());
        String playerColor = null;
        if(gameData.playerColor() != null) {
            playerColor = gameData.playerColor().toUpperCase();
        }
        String blackUsername = oldGame.blackUsername();
        String whiteUsername = oldGame.whiteUsername();
        ArrayList<UserData> observerList = oldGame.observerList();
        switch (playerColor) {
            case null:
                observerList.add(user);
                break;
            case "WHITE":
                if (oldGame.whiteUsername() == null) {
                    whiteUsername = user.username();
                    break;
                }
                throw new DataAccessException("Forbidden: White user already set", 403);
            case "BLACK":
                if (oldGame.blackUsername() == null) {
                    blackUsername = user.username();
                    break;
                }
                throw new DataAccessException("Forbidden: Black user already set", 403);
            default:
                throw new DataAccessException("Unexpected value: " + playerColor, 400);
        }
        Server.gameDAO.deleteGame(gameData);
        GameData newGame = new GameData(oldGame.gameID(), whiteUsername, blackUsername, oldGame.gameName(), oldGame.chessGame(), "", observerList);
        Server.gameDAO.addGame(newGame);
        return newGame;
    }
}
