package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.Response;

public class Serializer {
    static final Gson gson = new Gson();

    public static Object translateExceptionToJson(DataAccessException ex, Response res) {
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
}
