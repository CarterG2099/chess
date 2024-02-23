package serviceTests;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.DbService;
import service.GameService;
import service.UserService;
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
        dbService.clearData();
        existingUser = new UserData("ExistingUser","existingPassword", "existing@email.com");
        existingUser = new UserData("NewUser","newPassword", "new@email.com");

    }

    @BeforeEach
    public void setup() throws DataAccessException {
        dbService.clearData();
        registerResponse = userService.register(existingUser);
        existingAuthToken = registerResponse.authToken();

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
    public void registerBadInfo(){
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
    public void loginUnregisteredUser(){
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

    @Test public void validBadAuthToken() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.validAuthToken("badAuthToken");
        });
    }
}
