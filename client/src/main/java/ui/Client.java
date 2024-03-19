package ui;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import com.google.gson.Gson;

import static server.Serializer.*;

//catch exception in eval
public class Client {

    private static ServerFacade server;
    private static boolean loggedIn = false;
    public static void main(String[] args) {
        server = new ServerFacade();
        run();
    }

    public static void run() {
        System.out.println("Welcome to 240Chess!");
        System.out.print(loggedOutHelp());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            try{
                result = (String) eval(line);
                System.out.println(eval(line));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Object eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            if (loggedIn) {
                return loggedInMenu(cmd, params);
            } else {
                return loggedOutMenu(cmd, params);
            }
        }
        catch (DataAccessException ex) {
            return translateExceptionToJson(ex, null);
        }
    }

    private static String loggedInMenu(String cmd, String ...params) throws DataAccessException {
        return switch (cmd) {
            case "1" -> login(params);
            case "2" -> register(params);
            case "3" -> logout(params);
            case "4" -> listGames(params);
            case "5" -> createGame(params);
            case "6" -> joinRequest(params);
            case "7" -> quit();
            default -> help();
        };
    }

    private static String loggedOutMenu(String cmd, String ...params) throws DataAccessException {
        return switch (cmd) {
            case "1" -> login(params);
            case "2" -> register(params);
            case "4" -> quit();
            default -> help();
        };
    }
    public static String login(String ...params) throws DataAccessException {
        UserData loginRequest = new UserData(params[0], params[1], null);
        AuthData loginResponse = server.login(loginRequest);
        loggedIn = true;
        return loginResponse.authToken();
    }

    public static String register(String ...params) throws DataAccessException {
        UserData registerRequest = new UserData(params[0], params[1], params[2]);
        AuthData registerResponse = server.register(registerRequest);
        return registerResponse.authToken();
    }

    public static String logout(String ...params) throws DataAccessException {
        AuthData logoutRequest = new AuthData(null, params[0]);
        server.logout(logoutRequest);
        loggedIn = false;
        return "You have been logged out.";
    }

    public static String listGames(String ...params) throws DataAccessException {
        Gson gson = new Gson();
        AuthData listGamesRequest = new AuthData(null, params[0]);
        return gson.toJson(server.listGames(listGamesRequest));
    }

    public static String createGame(String ...params) throws DataAccessException {
        String createGameRequest = params[0];
        GameData createGameResponse = server.createGame(createGameRequest);
        return String.valueOf(createGameResponse.gameID());
    }

    public static String joinRequest(String ...params) throws DataAccessException {
        int gameID = Integer.parseInt(params[0]);
        GameData joinRequest = new GameData(gameID, null, null, null, null, params[0], null);
        server.joinRequest(joinRequest);
        return "Success";
    }

    private static String help() {
        if (loggedIn) {
            return loggedInHelp();
        } else {
            return loggedOutHelp();
        }
    }

    private static String quit() {
        return "quit";
    }
    public static String loggedInHelp() {
        return "Available commands:\n" +
                "  1. Logout <username> <password>\n" +
                "  2. Join <gameId>\n" +
                "  3. Observe <gameId>\n" +
                "  4. List Games\n" +
                "  5. Create Game\n" +
                "  6. Help\n" +
                "  7. Quit\n";
    }

    public static String loggedOutHelp() {
        return "Available commands:\n" +
                "  1. Login <username> <password>\n" +
                "  2. Register <username> <password>\n" +
                "  3. Help\n" +
                "  4. Quit\n";
    }

    //Implement methods here for menu
    //Calls UI.chessBoardUI when joining or observing game
    //Creates login request and register request...
}
