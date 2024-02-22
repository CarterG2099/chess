package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.UserService;
import spark.Request;
import spark.Response;

import javax.xml.crypto.Data;

public class UserHandler extends Server {
    private static final UserService userService = new UserService();
    public static Object register(Request req, Response res) {
        try{
            UserData userData = gson.fromJson(req.body(), UserData.class);
            return gson.toJson(userService.register(userData));
        } catch (DataAccessException ex) {
            return translateExceptionToJson(ex, res);
        }
    }

    public static Object login(Request req, Response res) {
        try {
            UserData userData = gson.fromJson(req.body(), UserData.class);
            return gson.toJson(userService.login(userData));
        } catch (DataAccessException ex) {
            return translateExceptionToJson(ex, res);
        }
    }

    public static Object logout(Request req, Response res) {
        try{
            String authToken = req.headers("Authorization");
            userService.validAuthToken(authToken);
            userService.logout(authToken);
            return translateSuccessToJson(res);
        } catch (DataAccessException ex) {
            return translateExceptionToJson(ex, res);
        }
    }
}
