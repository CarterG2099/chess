package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;
import model.UserData;
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
        int gameId = new Random().nextInt(1000);
        GameData newGame = new GameData(gameId, "", "", gameData.gameName(), new ChessGame(), "", new ArrayList<>());
        Server.gameDAO.addGame(newGame);
        return newGame;
    }

    public void joinGame(GameData gameData, UserData user) throws DataAccessException {
        GameData oldGame = Server.gameDAO.getGame(gameData.gameId());
        Server.gameDAO.deleteGame(gameData);
        String playerColor = gameData.playerColor();
        String blackUsername = "";
        String whiteUsername = "";
        ArrayList<UserData> observerLIst = oldGame.observerList();
        switch (playerColor){
            case "":
                observerLIst.add(user);
            case "White":
                whiteUsername = user.username();
            case "Black":
                blackUsername = user.username();
        }
        GameData newGame = new GameData(oldGame.gameId(),whiteUsername,blackUsername,oldGame.gameName(), oldGame.chessGame(), "", observerLIst);
        Server.gameDAO.addGame(newGame);
    }
}
