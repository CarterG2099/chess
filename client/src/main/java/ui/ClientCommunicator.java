package ui;

import com.google.gson.Gson;
import dataAccess.DataAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ClientCommunicator {
    //Use to actually contact server
    //Code from slides goes here - use them as a guide
    //Use IO slides to read and write from stream
    //Translate Objects to JSON

    private static String serverUrl = "http://localhost:8080";

    static <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeaders(authToken, http);
            writeBody(request, http);
            http.connect();
            return readBody(http, responseClass);
        } catch (Exception ex) {
            if (ex instanceof DataAccessException) {
                throw (DataAccessException) ex;
            } else {
                throw new DataAccessException("Failure" + ex.getMessage(), 500);
            }
        }
    }

    private static void writeHeaders(String authToken, HttpURLConnection http) throws DataAccessException {
        if (authToken != null) {
            http.setRequestProperty("Authorization", authToken);
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private static boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
