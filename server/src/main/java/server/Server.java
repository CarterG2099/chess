package server;

import dataAccess.*;
import model.UserData;
import spark.*;

public class Server {

    public static AuthDAO authDAO = new MemoryAuthDAO();
    public static UserDAO userDAO = new MemoryUserDAO();
    public static GameDAO gameDAO = new MemoryGameDAO();
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
//        DbServer dbServer = new DbServer();
        // Register your endpoints and handle exceptions here.
        Spark.post("/user", UserHandler::register);
        Spark.delete("/db", DbHandler::clearData);
        Spark.post("/session",UserHandler::login);
        Spark.delete("/session", UserHandler::logout);

        Spark.awaitInitialization();
        return Spark.port();
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
