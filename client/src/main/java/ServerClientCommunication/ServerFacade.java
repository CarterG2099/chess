package ServerClientCommunication;

import chess.ChessMove;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.StatusResponse;
import ui.Client;


public class ServerFacade {
    private static WebSocketCommunicator ws;

    public ServerFacade(int port) {
        HttpCommunicator.port = port;
    }

    public void clearData() throws DataAccessException {
        HttpCommunicator.makeRequest("DELETE", "/db", null, null, null);
    }

    public AuthData register(UserData registerRequest) throws DataAccessException {
        return HttpCommunicator.makeRequest("POST", "/user", registerRequest, null, AuthData.class);
    }

    public AuthData login(UserData loginRequest) throws DataAccessException {
        return HttpCommunicator.makeRequest("POST", "/session", loginRequest, null, AuthData.class);
    }

    public void logout(String authToken) throws DataAccessException {
        HttpCommunicator.makeRequest("DELETE", "/session", null, authToken, null);
    }

    public StatusResponse listGames(String authToken) throws DataAccessException {
        return HttpCommunicator.makeRequest("GET", "/game", null, authToken, StatusResponse.class);
    }

    public GameData createGame(GameData createGameRequest, String authToken) throws DataAccessException {
        return HttpCommunicator.makeRequest("POST", "/game", createGameRequest, authToken, GameData.class);
    }

    public void joinRequest(Client client, GameData gameData) throws DataAccessException {
        ws = new WebSocketCommunicator(client.serverUrl, client);
        ws.joinRequest(client.authToken, gameData);
//        return HttpCommunicator.makeRequest("PUT", "/game", joinRequest, authToken, GameData.class);
    }

    public void webSocketJoinRequest(Client client, GameData gameData, Boolean joinAsPlayer) throws DataAccessException {

    }

    public void leave(Client client, String playerColor) throws DataAccessException {
        ws = new WebSocketCommunicator(client.serverUrl, client);
        ws.leave(client.authToken, client.currentGame.gameID(), playerColor);
    }

    public void resign(Client client) throws DataAccessException {
        ws = new WebSocketCommunicator(client.serverUrl, client);
        ws.resign(client.authToken, client.currentGame.gameID());
    }

    public void makeMove(Client client, ChessMove move) throws DataAccessException {
        ws = new WebSocketCommunicator(client.serverUrl, client);
        ws.makeMove(client.authToken, client.currentGame.gameID(), move);
    }

}
