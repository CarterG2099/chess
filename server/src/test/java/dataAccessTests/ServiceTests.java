package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.DbService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ServiceTests {

    private static final GameService gameService = new GameService();
    private static final UserService userService = new UserService();
    private static final DbService dbService = new DbService();
    private static UserData existingUser;

    private static UserData newUser;
    private static String existingAuthToken;
    private AuthData registerResponse;

    private static Server server;


    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        Server.setMySqlDAOs();
        dbService.clearData();
        existingUser = new UserData("ExistingUser", "existingPassword", "existing@email.com");
        existingUser = new UserData("NewUser", "newPassword", "new@email.com");

    }

    @BeforeEach
    public void setup() throws DataAccessException {
        dbService.clearData();
        registerResponse = userService.register(existingUser);
        existingAuthToken = registerResponse.authToken();

    }

    @Test
    public void clearGameData() throws DataAccessException {
        dbService.clearData();
        ArrayList<GameData> games = Server.gameDAO.getGameList();
        Assertions.assertEquals(0, games.size());
    }

    @Test
    public void clearUserData() throws DataAccessException {
        dbService.clearData();
        UserData user = Server.userDAO.getUser(existingUser.username());
        Assertions.assertNull(user);
    }

    @Test
    public void clearAuthData() throws DataAccessException {
        dbService.clearData();
        AuthData auth = Server.authDAO.getAuthToken(existingAuthToken);
        Assertions.assertNull(auth);
    }

    @Test
    public void registerSuccess() throws DataAccessException {
        dbService.clearData();
        AuthData registerResult = userService.register(existingUser);
        Assertions.assertEquals(existingUser.username(), registerResult.username());
    }

    @Test
    public void registerDuplicate() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(existingUser);
        });
    }

    @Test
    public void registerBadInfo() {
        UserData incompleteUser = new UserData("Incomplete", null, "incomplete email");
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(incompleteUser);
        });
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        AuthData loginResult = userService.login(existingUser);
        Assertions.assertEquals(loginResult.username(), existingUser.username());
    }

    @Test
    public void loginUnregisteredUser() {
        UserData unregisteredUSer = new UserData("Unregistered", "no password", "no email");
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(unregisteredUSer);
        });
    }

    @Test
    public void logoutSuccess() throws DataAccessException {
        userService.logout(existingAuthToken);
        Assertions.assertThrows(NullPointerException.class, () -> {
            Server.authDAO.getAuthToken(Server.authDAO.getAuthToken(existingAuthToken).authToken());
        });
    }

    @Test
    public void logoutBadToken() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout("BadToken");
        });
    }

    @Test
    public void validAuthTokenSuccess() throws DataAccessException {
        Assertions.assertTrue(userService.validAuthToken(existingAuthToken));
    }

    @Test
    public void validBadAuthToken() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.validAuthToken("badAuthToken");
        });
    }

    @Test
    public void getUserSuccess() throws DataAccessException {
        Assertions.assertEquals(existingUser, userService.getUser(existingAuthToken));
    }

    @Test
    public void getUserBadToken() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            userService.getUser("BadToken");
        });
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        GameData gameToAdd = new GameData(0, "white", "black", "testGame", new ChessGame(), "Color",  new ArrayList<>());
        GameData game = gameService.createGame(gameToAdd);
        Assertions.assertEquals(gameToAdd.gameName(), game.gameName());

    }

    @Test
    public void createGameNoName() {
        GameData gameToAdd = new GameData(0, "white", "black", null, new ChessGame(), "Color",  new ArrayList<>());
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(gameToAdd);
        });
    }

    @Test
    public void getGamesSuccess() throws DataAccessException {
        GameData gameToAdd = new GameData(1234, "white", "black", "testGame", new ChessGame(), "Color",  new ArrayList<>());
        gameService.createGame(gameToAdd);
        ArrayList<GameData> gameList = gameService.getGames();
        Assertions.assertEquals(gameToAdd.gameName(), gameList.get(0).gameName());
    }

    @Test
    public void getGamesEmpty() throws DataAccessException {
        ArrayList<GameData> gameList = gameService.getGames();
        Assertions.assertEquals(0, gameList.size());
    }

    @Test
    public void deleteGameSuccess() throws DataAccessException {
        GameData gameToAdd = new GameData(1234, "white", "black", "testGame", new ChessGame(), "Color",  new ArrayList<>());
        Server.gameDAO.addGame(gameToAdd);
        Server.gameDAO.deleteGame(gameToAdd);
        ArrayList<GameData> gameList = gameService.getGames();
        Assertions.assertEquals(0, gameList.size());
    }

    @Test
    public void deleteGameNoGame() {
        GameData gameToAdd = new GameData(1234, "white", "black", "testGame", new ChessGame(), "Color",  new ArrayList<>());
        Assertions.assertThrows(DataAccessException.class, () -> {
            Server.gameDAO.deleteGame(gameToAdd);
        });
    }

//    @Test
//    public void joinGameSuccess() throws DataAccessException {
//        GameData gameToAdd = new GameData(1234, "white", "black", "testGame", new ChessGame(), "WHITE",  new ArrayList<>());
//        Server.gameDAO.addGame(gameToAdd);
//        gameService.joinGame(gameToAdd, existingUser);
//        GameData game = Server.gameDAO.getGame(gameToAdd.gameID());
//        Assertions.assertEquals(existingUser.username(), game.whiteUsername());
//    }
}
