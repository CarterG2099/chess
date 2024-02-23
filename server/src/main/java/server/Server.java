package server;

import com.google.gson.Gson;
import dataAccess.*;
import spark.Response;
import spark.Spark;

public class Server {

    public static AuthDAO authDAO = new MemoryAuthDAO();
    public static UserDAO userDAO = new MemoryUserDAO();
    public static GameDAO gameDAO = new MemoryGameDAO();
    static final Gson gson = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.post("/user", UserHandler::register);
        Spark.delete("/db", DbHandler::clearData);
        Spark.post("/session", UserHandler::login);
        Spark.delete("/session", UserHandler::logout);
        Spark.get("/game", GameHandler::listGames);
        Spark.post("/game", GameHandler::createGame);
        Spark.put("/game", GameHandler::joinRequest);

        Spark.awaitInitialization();
        return Spark.port();
    }

    static Object translateExceptionToJson(DataAccessException ex, Response res) {
        StatusResponse statusResponse = new StatusResponse(ex.getMessage(), ex.getStatusCode(), null, null);
        res.status(ex.getStatusCode());
        res.body(ex.getMessage());
        return gson.toJson(statusResponse);
    }

    static Object translateSuccessToJson(Response res) {
        StatusResponse statusResponse = new StatusResponse("Success", 200, null, null);
        res.status(200);
        res.body("Success");
        return gson.toJson(statusResponse);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
