package server;

import dataAccess.DataAccessException;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import static server.Serializer.*;

public class GameHandler extends Server {
    private static final GameService gameService = new GameService();
    private static final UserService userService = new UserService();

    public static Object createGame(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            userService.validAuthToken(authToken);
            GameData gameData = gson.fromJson(req.body(), GameData.class);
            gameData = gameService.createGame(gameData);
            StatusResponse statusResponse = new StatusResponse("Success", 200, String.valueOf(gameData.gameID()), null);
            return gson.toJson(gameData);
        } catch (DataAccessException ex) {
            return translateExceptionToJson(ex, res);
        }
    }


    public static Object listGames(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            userService.validAuthToken(authToken);
            StatusResponse statusResponse = new StatusResponse("Success", 200, null, gameService.getGames());
            return gson.toJson(statusResponse);
        } catch (DataAccessException ex) {
            return translateExceptionToJson(ex, res);
        }
    }

    public static Object joinRequest(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            userService.validAuthToken(authToken);
            UserData user = userService.getUser(authToken);
            GameData gameData = gson.fromJson(req.body(), GameData.class);
            gameService.joinGame(gameData, user);
            GameData returnState = gameService.getGame(gameData.gameID());
            return gson.toJson(returnState);
        } catch (DataAccessException ex) {
            return translateExceptionToJson(ex, res);
        }
    }
}
