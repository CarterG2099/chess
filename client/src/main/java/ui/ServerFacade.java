package ui;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.StatusResponse;

public class ServerFacade {
    //One method per endpoint - 7 total with 2-3 lines of code
    //Class to represent server and then uses client communicator to actually call server


    public void clearData() throws DataAccessException {
        ClientCommunicator.makeRequest("POST", "/db", null, null, null);
    }

    public AuthData register(UserData registerRequest) throws DataAccessException {
        return ClientCommunicator.makeRequest("POST", "/user", registerRequest, null, AuthData.class);
    }

    public AuthData login(UserData loginRequest) throws DataAccessException {
        return ClientCommunicator.makeRequest("POST", "/session", loginRequest, null, AuthData.class);
    }

    public void logout(String authToken) throws DataAccessException {
        ClientCommunicator.makeRequest("DELETE", "/session", null, authToken, null);
    }

    public StatusResponse listGames(String authToken) throws DataAccessException {
        return ClientCommunicator.makeRequest("GET", "/game", null, authToken, StatusResponse.class);
    }

    public GameData createGame(GameData createGameRequest, String authToken) throws DataAccessException {
        return ClientCommunicator.makeRequest("POST", "/game", createGameRequest, authToken, GameData.class);
    }

    public GameData joinRequest(GameData joinRequest, String authToken) throws DataAccessException {
        return ClientCommunicator.makeRequest("PUT", "/game", joinRequest, authToken, GameData.class);
    }
}
