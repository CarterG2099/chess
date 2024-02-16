package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class DbServer extends Server {
    public static Object clearData(Request req, Response res) {
        ClearService clearService = new ClearService();
        Gson gson = new Gson();

        try {
            clearService.clearData();
            StatusResponse statusResponse = new StatusResponse(200);
            return gson.toJson(statusResponse);
        } catch (DataAccessException e) {
            res.status(500); // Internal Server Error
            return gson.toJson("Error clearing " + e.getMessage());
        }
    }
}
