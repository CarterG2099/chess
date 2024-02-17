package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler extends Server {

    private static final Gson gson = new Gson();
    private static final UserService userService = new UserService();
    public static Object register(Request req, Response res) {
        try{
            UserData userData = translateFromJson(req);
            return gson.toJson(userService.register(userData));
        } catch (DataAccessException ex) {
            res.status(500); // Internal Server Error
            return gson.toJson("Error: " + ex.getMessage());
        }
    }

    public static Object login(Request req, Response res) {
        try {
            UserData userData = translateFromJson(req);
            return gson.toJson(userService.login(userData));
        } catch (DataAccessException ex) {
            res.status(500);
            return gson.toJson("Error logging in");
        }
    }

    public static Object logout(Request req, Response res) {
        try{
            UserData userData = translateFromJson(req);
            return gson.toJson(userService.logout(userData));
        } catch (DataAccessException ex) {
            res.status(500);
            return gson.toJson("Error logging out");
        }
    }

    private static UserData translateFromJson(Request req){
        return gson.fromJson(req.body(), UserData.class);
    }
}
