package ui;

import chess.*;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import static server.Serializer.translateExceptionToJson;

//catch exception in eval
public class Client extends Endpoint implements EscapeSequences.ServerMessageObserver {

    private Session session;
    private static ServerFacade serverFacade;
    private static boolean loggedIn = false;
    private static String authToken;
    private static ArrayList<GameData> gameList = new ArrayList<>();

    private static ChessGame currentGame;

    private static ChessGame.TeamColor playerColor;

    public static void main(String[] args) {
        serverFacade = new ServerFacade(8080);
        run();
    }

    public Client() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
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

    public void notify(ServerMessage message) {
        System.out.println(message);
    }

    private static String loggedInMenu(String cmd, String... params) throws DataAccessException {
        return switch (cmd) {
            case "1" -> logout();
            case "2" -> joinRequest(params);
            case "3" -> listGames();
            case "4" -> createGame(params);
            case "5" -> "Redraw Chess Board";
            case "6" -> "Leave";
            case "7" -> "Resign";
            case "8" -> "Highlight Legal Moves";
            case "10" -> quit();
            case "clear" -> {
                serverFacade.clearData();
                yield "Data Cleared";
            }
            default -> help();
        };
    }

    public static String loggedInHelp() {
        return """
                Available commands:
                  1. Logout
                  2. Join <gameId> <Black?/White?>
                  3. List Games
                  4. Create Game <gameName>
                  5. Redraw Chess Board
                  6. Leave
                  7. Resign
                  8. Highlight Legal Moves
                  9. Help
                  10. Quit
                """;
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

    public static String highlightLegalMoves(String... params) {
        if (params.length < 1) {
            return "Please specify a piece to highlight legal moves for.";
        }
        ChessPosition position = currentGame.getBoard().findChessPiece(playerColor, params[0].toUpperCase());
        Collection<ChessMove> legalMoves = currentGame.validMoves(position);
        ChessBoardUI.drawChessBoard(currentGame.getBoard(), playerColor, legalMoves);
        return "Highlight Legal Moves";
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
        gameList = serverFacade.listGames(authToken).games();
        String gameListString = "";
        if (gameList.isEmpty()) {
            return "No games available";
        }
        for (int i = 0; i < gameList.size(); i++) {
            GameData game = gameList.get(i);
            gameListString += (i + 1) + ". " + game.gameName() + "\n";
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
        gameList.add(createGameResponse);
        currentGame = createGameResponse.chessGame();
        return "Game " + params[0] + " created with ID: " + gameID;
    }

    public static String joinRequest(String... params) throws DataAccessException {
        if (params.length < 1) {
            throw new DataAccessException("Bad Request", 400);
        }
        String playerColor = null;
        if (params.length > 1) {
            playerColor = params[1].toUpperCase();
        }
        GameData gameToJoin = gameList.get(Integer.parseInt(params[0]) - 1);
        int gameID = gameToJoin.gameID();
        GameData joinRequest = new GameData(gameID, null, null, null, null, playerColor, null);
        GameData gameData = serverFacade.joinRequest(joinRequest, authToken);
        ChessBoard chessBoard = gameData.chessGame().getBoard();
        ChessBoardUI.drawChessBoards(chessBoard);
        currentGame = gameData.chessGame();
        if (playerColor == null) {
            return "You have joined " + gameList.get(Integer.parseInt(params[0]) - 1).gameName() + " as an observer.";
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


    public static String loggedOutHelp() {
        return """
                Available commands:
                  1. Login <username> <password>
                  2. Register <username> <password> <email>
                  3. Help
                  4. Quit
                """;
    }

    //Implement methods here for menu
    //Calls UI.chessBoardUI when joining or observing game
    //Creates login request and register request...
}
