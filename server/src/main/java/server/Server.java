package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.UserData;
import spark.*;

public class Server {

    public static AuthDAO authDAO = new MemoryAuthDAO();
    public static UserDAO userDAO = new MemoryUserDAO();
    public static GameDAO gameDAO = new MemoryGameDAO();
    static final Gson gson = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
//        DbServer dbServer = new DbServer();
        // Register your endpoints and handle exceptions here.
        Spark.post("/user", UserHandler::register);
        Spark.delete("/db", DbHandler::clearData);
        Spark.post("/session", UserHandler::login);
        Spark.delete("/session", UserHandler::logout);

        Spark.awaitInitialization();
        return Spark.port();
    }

    static UserData translateFromJson(Request req){
        return gson.fromJson(req.body(), UserData.class);
    }

    static Object translateExceptionToJson(DataAccessException ex, Response res){
        StatusResponse statusResponse = new StatusResponse(ex.getMessage(), ex.getStatusCode());
        res.status(ex.getStatusCode());
        res.body(ex.getMessage());
        return gson.toJson(statusResponse);
    }
    static Object translateSuccessToJson(){
        StatusResponse statusResponse = new StatusResponse("Success", 200);
        return gson.toJson(statusResponse);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
