package server;

import spark.Request;
import spark.Response;

public class UserServer extends Server {
    public static String register(Request req, Response res) {
        return "Success";
    }
}
