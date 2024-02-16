package server;

import spark.Request;
import spark.Response;

public class userServer extends Server {
    public static String register(Request req, Response res) {
        return "Success";
    }
}
