package server;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import javax.xml.crypto.Data;

public class GameHandler extends Server {
    private static final GameService gameService = new GameService();
    private static final UserService userService = new UserService();

    public static Object createGame(Request req, Response res){
        try{
            String authToken = req.headers("Authorization");
            userService.validAuthToken(authToken);
            GameData gameData = gson.fromJson(req.body(), GameData.class);
            gameData = gameService.createGame(gameData);
            StatusResponse statusResponse = new StatusResponse(String.valueOf(gameData.gameId()), 200);
            return gson.toJson(statusResponse);
        } catch(DataAccessException ex){
            return translateExceptionToJson(ex, res);
        }
    }


    public static Object listGames(Request req, Response res){
        try{
            String authToken = req.headers("Authorization");
            userService.validAuthToken(authToken);
            return gson.toJson(gameService.getGames());

        }catch(DataAccessException ex) {
            return translateExceptionToJson(ex, res);
        }
    }

    public static Object joinGame(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            userService.validAuthToken(authToken);
            UserData user = userService.getUser(authToken);
            GameData gameData = gson.fromJson(req.body(), GameData.class);
            gameService.joinGame(gameData, user);
            return translateSuccessToJson(res);
        } catch (DataAccessException ex){
            return translateExceptionToJson(ex, res);
        }
    }
}
