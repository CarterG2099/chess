package clientTests;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.StatusResponse;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    static ServerFacade serverFacade;
    private static final UserData registerRequest = new UserData("username", "password", "email");
    private static AuthData registerResponse;

    @BeforeAll
    public static void init() {
        var port = Server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        Server.stop();
    }

    @BeforeEach
    void clearData() throws DataAccessException {
        serverFacade.clearData();
    }

    @Test
    void register() throws Exception {
        registerResponse = serverFacade.register(registerRequest);
        assertTrue(registerResponse.authToken().length() > 10);
    }

    @Test
    void registerBad() throws DataAccessException {
        UserData registerRequestBad = new UserData(null, "password", "email");
        serverFacade.register(registerRequestBad);
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.register(registerRequest));
    }

    @Test
    void login() throws Exception {
        serverFacade.register(registerRequest);
        registerResponse = serverFacade.login(registerRequest);
        assertTrue(registerResponse.authToken().length() > 10);
    }

    @Test
    void loginBad() throws DataAccessException {
        serverFacade.register(registerRequest);
        UserData loginRequest = new UserData("badUsername", "password", null);
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.login(loginRequest));
    }

    @Test
    void logout() throws DataAccessException {
        registerResponse = serverFacade.register(registerRequest);
        serverFacade.logout(registerResponse.authToken());
    }

    @Test
    void logoutBad() {
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.logout("badToken"));
    }

    @Test
    void listGames() throws DataAccessException {
        registerResponse = serverFacade.register(registerRequest);
        StatusResponse listGamesResponse = serverFacade.listGames(registerResponse.authToken());
        assertTrue(listGamesResponse.games().isEmpty());
    }

    @Test
    void listGamesBad() {
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.listGames("badToken"));
    }

    @Test
    void createGame() throws DataAccessException {
        GameData createGameRequest = new GameData(null, null, null, "gameName", null, null, null);
        registerResponse= serverFacade.register(registerRequest);
        GameData createGameResponse = serverFacade.createGame(createGameRequest, registerResponse.authToken());
        assertEquals("gameName", createGameResponse.gameName());
    }

    @Test
    void createGameBad() throws DataAccessException {
        GameData createGameRequest = new GameData(null, null, null, null, null, null, null);
        registerResponse = serverFacade.register(registerRequest);
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.createGame(createGameRequest, registerResponse.authToken()));
    }

    @Test
    void joinRequest() throws DataAccessException {
        GameData createGameRequest = new GameData(0, null, null, "gameName", null, null, null);
        registerResponse = serverFacade.register(registerRequest);
        GameData createGameResponse = serverFacade.createGame(createGameRequest, registerResponse.authToken());
        GameData joinRequest = new GameData(createGameResponse.gameID(), "white", null, null, null, null, null);
        GameData joinResponse = serverFacade.joinRequest(joinRequest, registerResponse.authToken());
        assertEquals("white", joinResponse.whiteUsername());
    }

    @Test
    void joinRequestBad() throws DataAccessException {
        GameData joinRequest = new GameData(0, "white", null, null, null, null, null);
        registerResponse = serverFacade.register(registerRequest);
        Assertions.assertThrows(DataAccessException.class, () -> serverFacade.joinRequest(joinRequest, registerResponse.authToken()));
    }
}
