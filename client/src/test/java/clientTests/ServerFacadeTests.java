package clientTests;

import dataAccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach void clearData() throws DataAccessException {
        serverFacade.clearData();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void register() throws Exception {
        UserData registerRequest = new UserData("username", "password", "email");
        var authData = serverFacade.register(registerRequest);
        assertTrue(authData.authToken().length() > 10);
    }
}
