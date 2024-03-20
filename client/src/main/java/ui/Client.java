package ui;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static server.Serializer.translateExceptionToJson;

//catch exception in eval
public class Client {

    private static ServerFacade serverFacade;
    private static boolean loggedIn = false;
    private static String authToken;

    public static void main(String[] args) {
        serverFacade = new ServerFacade();
        run();
    }

    public static void run() {
        System.out.println("Welcome to 240Chess!");
        System.out.print(loggedOutHelp());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            try {
                result = (String) eval(line);
                System.out.println(result);
            } catch (Exception e) {
                if (e instanceof DataAccessException) {
                    System.out.println(translateExceptionToJson((DataAccessException) e, null));
                }
                System.out.println(e.getMessage());
            }
        }
    }

    public static Object eval(String input) {
        System.out.println("Press Enter for a list of commands");
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            if (loggedIn) {
                return loggedInMenu(cmd, params);
            } else {
                return loggedOutMenu(cmd, params);
            }
        } catch (DataAccessException ex) {
            return ex.getMessage() + " " + ex.getStatusCode();
        }
    }

    private static String loggedInMenu(String cmd, String... params) throws DataAccessException {
        return switch (cmd) {
            case "1" -> logout();
            case "2" -> joinRequest(params);
            case "3" -> listGames();
            case "4" -> createGame(params);
            case "6" -> quit();
            case "clear" -> {
                serverFacade.clearData();
                yield "Data Cleared";
            }
            default -> help();
        };
    }

    private static String loggedOutMenu(String cmd, String... params) throws DataAccessException {
        return switch (cmd) {
            case "1" -> login(params);
            case "2" -> register(params);
            case "4" -> quit();
            case "clear" -> {
                serverFacade.clearData();
                yield "Data Cleared";
            }
            default -> help();
        };
    }

    public static String login(String... params) throws DataAccessException {
        if (params.length < 2) {
            throw new DataAccessException("Bad Request", 400);
        }
        UserData loginRequest = new UserData(params[0], params[1], null);
        AuthData loginResponse = serverFacade.login(loginRequest);
        loggedIn = true;
        authToken = loginResponse.authToken();
        return "You have been logged in.";
    }

    public static String register(String... params) throws DataAccessException {
        if (params.length < 3) {
            throw new DataAccessException("Bad Request", 400);
        }
        UserData registerRequest = new UserData(params[0], params[1], params[2]);
        AuthData registerResponse = serverFacade.register(registerRequest);
        authToken = registerResponse.authToken();
        loggedIn = true;
        return "You have been registered and logged in.";
    }

    public static String logout() throws DataAccessException {
        serverFacade.logout(authToken);
        loggedIn = false;
        return "You have been logged out.";
    }

    public static String listGames() throws DataAccessException {
        ArrayList<GameData> gameList = serverFacade.listGames(authToken).games();
        String gameListString = "";
        for (GameData game : gameList) {
            gameListString += game.gameID() + " " + game.gameName() + "\n";
        }
        if (gameListString.isEmpty()) {
            return "No games available";
        }
        return gameListString;
    }

    public static String createGame(String... params) throws DataAccessException {
        if (params.length < 1) {
            throw new DataAccessException("Bad Request", 400);
        }
        GameData createGameRequest = new GameData(0, null, null, params[0], null, null, null);
        GameData createGameResponse = serverFacade.createGame(createGameRequest, authToken);
        String gameID = String.valueOf(createGameResponse.gameID());
        return "Game created with ID: " + gameID;
    }

    public static String joinRequest(String... params) throws DataAccessException {
        if (params.length < 1) {
            throw new DataAccessException("Bad Request", 400);
        }
        String playerColor = null;
        if (params.length > 1) {
            playerColor = params[1].toUpperCase();
        }
        int gameID = Integer.parseInt(params[0]);
        GameData joinRequest = new GameData(gameID, null, null, null, null, playerColor, null);
        serverFacade.joinRequest(joinRequest, authToken);
        if (playerColor == null) {
            return "You have joined the game as an observer.";
        }
        if (playerColor.equals("BLACK")) {
            return "You have joined the game as black.";
        }
        if (playerColor.equals("WHITE")) {
            return "You have joined the game as white.";
        }
        return "Invalid color";
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
                "  1. Logout\n" +
                "  2. Join <gameId> <Black?/White?>\n" +
                "  3. List Games\n" +
                "  4. Create Game <gameName>\n" +
                "  5. Help\n" +
                "  6. Quit\n";
    }

    public static String loggedOutHelp() {
        return "Available commands:\n" +
                "  1. Login <username> <password>\n" +
                "  2. Register <username> <password> <email>\n" +
                "  3. Help\n" +
                "  4. Quit\n";
    }

    //Implement methods here for menu
    //Calls UI.chessBoardUI when joining or observing game
    //Creates login request and register request...
}
