package server;

import DataAccessException.DataAccessException;
import chess.ChessGame;
import chess.ChessPiece;
import dataAccess.*;
import server.websocket.WebSocketHandler;
import spark.Spark;

public class Server {

    public static AuthDAO authDAO;
    public static UserDAO userDAO;
    public static GameDAO gameDAO;

    public static void main(String[] args) throws DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        run(8080);
    }

    public static int run(int desiredPort) {
        Spark.port(desiredPort);
//        setMemoryDAOs();
        setMySqlDAOs();

        Spark.staticFiles.location("web");
        Spark.webSocket("/connect", WebSocketHandler.class);
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


//    private static void setMemoryDAOs() {
//        authDAO = new MemoryAuthDAO();
//        userDAO = new MemoryUserDAO();
//        gameDAO = new MemoryGameDAO();
//    }

    public static void setMySqlDAOs() {
        authDAO = new MySqlAuthDAO();
        userDAO = new MySqlUserDAO();
        gameDAO = new MySqlGameDAO();
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e) {
            Serializer.translateExceptionToJson(e, null);
        }
    }


    public static void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
