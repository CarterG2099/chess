package server;

import com.google.gson.Gson;
import service.ClearService;
import spark.Request;
import spark.Response;

public class DbServer extends Server {
    public static String clearData(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        String jsonReq = gson.toJson(req);
        ClearService clearService = new ClearService();
        clearService.deleteAuthToken();
        return "Success";


    }
}
