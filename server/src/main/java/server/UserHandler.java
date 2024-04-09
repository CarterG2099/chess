package server;

import DataAccessException.DataAccessException;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

import static server.Serializer.*;

public class UserHandler extends Server {
    private static final UserService userService = new UserService();

    public static Object register(Request req, Response res) {
        try {
            UserData userData = gson.fromJson(req.body(), UserData.class);
            return gson.toJson(userService.register(userData));
        } catch (DataAccessException ex) {
            return Serializer.translateExceptionToJson(ex, res);
        }
    }

    public static Object login(Request req, Response res) {
        try {
            UserData userData = gson.fromJson(req.body(), UserData.class);
            return gson.toJson(userService.login(userData));
        } catch (DataAccessException ex) {
            return Serializer.translateExceptionToJson(ex, res);
        }
    }

    public static Object logout(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            userService.validAuthToken(authToken);
            userService.logout(authToken);
            return translateSuccessToJson(res);
        } catch (DataAccessException ex) {
            return translateExceptionToJson(ex, res);
        }
    }
}
