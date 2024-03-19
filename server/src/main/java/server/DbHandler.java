package server;

import dataAccess.DataAccessException;
import service.DbService;
import spark.Request;
import spark.Response;

import static server.Serializer.*;

public class DbHandler extends Server {
    public static Object clearData(Request req, Response res) {
        DbService dbService = new DbService();
        try {
            dbService.clearData();
            return translateSuccessToJson(res);
        } catch (DataAccessException ex) {
            return translateExceptionToJson(ex, res);
        }
    }
}
