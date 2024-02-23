package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestException;
import passoffTests.testClasses.TestModels;
import server.Server;
import service.DbService;
import service.GameService;
import service.UserService;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
public class ServiceTests {

    private static final GameService gameService = new GameService();
    private static final UserService userService = new UserService();
    private static final DbService dbService = new DbService();
    private static String existingUserUsername;
    private static String existingUserPassword;
    private static String existingUserEmail;

    private static String newUserUsername;
    private static String newUserPassword;
    private static String newUserEmail;



    @BeforeAll
    public static void init() throws DataAccessException {
        dbService.clearData();
        existingUserUsername = "ExistingUser";
        existingUserPassword = "existingUserPassword";
        existingUserEmail = "eu@mail.com";

        newUserUsername = "NewUser";
        newUserPassword = "newUserPassword";
        newUserEmail = "nu@mail.com";
    }
}
