package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import service.DbService;
import spark.Request;
import spark.Response;

public class DbHandler extends Server {
    public static Object clearData(Request req, Response res) {
        DbService dbService = new DbService();
        Gson gson = new Gson();

        try {
            dbService.clearData();
            StatusResponse statusResponse = new StatusResponse(200);
            return gson.toJson(statusResponse);
        } catch (DataAccessException ex) {
            res.status(500); // Internal Server Error
            return gson.toJson("Error clearing " + ex.getMessage());
        }
    }
}
