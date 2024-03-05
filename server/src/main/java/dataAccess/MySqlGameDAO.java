package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import javax.xml.crypto.Data;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlGameDAO implements GameDAO{
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
        int game_id = gameData.gameID();
        String game_name = gameData.gameName();
        String white_username = gameData.whiteUsername();
        String black_username = gameData.blackUsername();
        ArrayList<UserData> observer_list = new ArrayList<>();
        var statement = "INSERT INTO game_data (game_id, white_username, black_username, game_name, chess_game, player_color, observer_list) VALUES (?, ?, ?, ?, ?, ?, ?)";
        DatabaseManager.executeUpdate(statement, game_id, white_username, black_username, game_name, null, null, observer_list);
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
        return null;    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {
        var statement = "DELETE FROM game_data WHERE game_id = ?";
        if(DatabaseManager.executeUpdate(statement, game.gameID()) == 0){
            throw new DataAccessException("No game found", 400);
        }

    }

    private GameData readGameData(ResultSet rs) throws DataAccessException, SQLException {
        Gson gson = new Gson();
        var game_id = rs.getInt("game_id");
        var white_username = rs.getString("white_username");
        var black_username = rs.getString("black_username");
        var game_name = rs.getString("game_name");
        var chess_game_var = rs.getString("chess_game");
        ChessGame chess_game = new ChessGame();
        if(chess_game_var != null){
            chess_game = gson.fromJson(chess_game_var, ChessGame.class);
        }
        var player_color = rs.getString("player_color");
        var observer_var = rs.getString("observer_list");
        Type listType = new TypeToken<ArrayList<UserData>>() {}.getType();
        ArrayList<UserData> observer = gson.fromJson(observer_var, listType);
        return new GameData(game_id, white_username, black_username, game_name, chess_game, player_color, observer);
    }
}
