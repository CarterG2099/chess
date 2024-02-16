package server;

import com.google.gson.Gson;
import service.ClearService;
import spark.Request;
import spark.Response;

import javax.xml.catalog.Catalog;

public class dbServer extends Server {
    public static String clearData(Request req, Response res) {
//        res.type("application/json");
//        Gson gson = new Gson();
//        String jsonReq = gson.toJson(req);
//        deleteAuthToken(jsonReq);
        ClearService.deleteAuthToken();
        return "Success";


    }
}
