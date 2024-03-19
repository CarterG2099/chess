package ui;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

public class ServerFacade {
    //One method per endpoint - 7 total with 2-3 lines of code
    //Class to represent server and then uses client communicator to actually call server




    public void clearData() throws DataAccessException {
        ClientCommunicator.makeRequest("POST", "/db", null, null);
    }

    public AuthData register(UserData registerRequest) throws DataAccessException {
        return ClientCommunicator.makeRequest("POST", "/user", registerRequest, AuthData.class);
    }

    public AuthData login(UserData loginRequest) throws DataAccessException {
        return ClientCommunicator.makeRequest("POST", "/session", loginRequest, AuthData.class);
    }

    public void logout(AuthData logoutRequest) throws DataAccessException {
        ClientCommunicator.makeRequest("DELETE", "/session", logoutRequest, null);
    }

    public GameData listGames(AuthData listGamesRequest) throws DataAccessException {
        return ClientCommunicator.makeRequest("GET", "/game", listGamesRequest, GameData.class);
    }

    public GameData createGame(String createGameRequest) throws DataAccessException {
        return ClientCommunicator.makeRequest("POST", "/game",createGameRequest, GameData.class);
    }

    public Object joinRequest(GameData joinRequest) throws DataAccessException {
        return ClientCommunicator.makeRequest("PUT", "/game", null, null);
    }

}
