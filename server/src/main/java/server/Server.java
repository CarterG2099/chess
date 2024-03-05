package server;

import com.google.gson.Gson;
import dataAccess.*;
import spark.Response;
import spark.Spark;

import javax.xml.crypto.Data;
import java.sql.Connection;

public class Server {

    public static AuthDAO authDAO;
    public static UserDAO userDAO;
    public static GameDAO gameDAO;
    static final Gson gson = new Gson();

    public int run(int desiredPort){
        Spark.port(desiredPort);
//        setMemoryDAOs();
        setMySqlDAOs();

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
        if(res != null) {
            res.status(ex.getStatusCode());
            res.body(ex.getMessage());
        }
        StatusResponse statusResponse = new StatusResponse(ex.getMessage(), ex.getStatusCode(), null, null);
        return gson.toJson(statusResponse);
    }

    static Object translateSuccessToJson(Response res) {
        StatusResponse statusResponse = new StatusResponse("Success", 200, null, null);
        res.status(200);
        res.body("Success");
        return gson.toJson(statusResponse);
    }

    private static void setMemoryDAOs() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
    }

    public static void setMySqlDAOs() {
        authDAO = new MySqlAuthDAO();
        userDAO = new MySqlUserDAO();
        gameDAO = new MySqlGameDAO();
        try{
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e) {
            translateExceptionToJson(e, null);
        }
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
