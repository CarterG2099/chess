package dataAccess;

import DataAccessException.DataAccessException;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.GameData;
import model.UserData;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlGameDAO implements GameDAO {
    @Override
    public void deleteGameData() throws DataAccessException {
        var statement = "DELETE FROM game_data";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public ArrayList<GameData> getGameList() throws DataAccessException {
        ArrayList<GameData> gameDataArrayList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game_data";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        gameDataArrayList.add(readGameData(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return gameDataArrayList;
    }

    @Override
    public void addGame(GameData gameData) throws DataAccessException {
        int gameID = gameData.gameID();
        String gameName = gameData.gameName();
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        ArrayList<UserData> observerList = gameData.observerList();
        ChessGame chessGame = gameData.chessGame();
        var statement = "INSERT INTO game_data (game_id, white_username, black_username, game_name, chess_game, player_color, observer_list) VALUES (?, ?, ?, ?, ?, ?, ?)";
        DatabaseManager.executeUpdate(statement, gameID, whiteUsername, blackUsername, gameName, chessGame, null, observerList);
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game_data WHERE game_id = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGameData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {
        var statement = "DELETE FROM game_data WHERE game_id = ?";
        if (DatabaseManager.executeUpdate(statement, game.gameID()) == 0) {
            throw new DataAccessException("No game found", 400);
        }

    }

    private GameData readGameData(ResultSet rs) throws SQLException {
        Gson gson = new Gson();
        var gameId = rs.getInt("game_id");
        var whiteUsername = rs.getString("white_username");
        var blackUsername = rs.getString("black_username");
        var gameName = rs.getString("game_name");
        var chessGameVar = rs.getString("chess_game");
        ChessGame chessGame;
        if (chessGameVar != null) {
            chessGame = gson.fromJson(chessGameVar, ChessGame.class);
        } else {
            chessGame = new ChessGame();
        }
        var playerColor = rs.getString("player_color");
        var observerList = rs.getString("observer_list");
        Type listType = new TypeToken<ArrayList<UserData>>() {}.getType();
        ArrayList<UserData> observer = gson.fromJson(observerList, listType);
        return new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame, playerColor, observer);
    }
}
