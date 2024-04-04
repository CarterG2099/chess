package ui;

import ServerClientCommunication.*;
import chess.*;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.Serializer;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.*;

import static server.Serializer.translateExceptionToJson;

public class Client implements ServerMessageObserver {

    private static ServerFacade serverFacade;
    private static boolean loggedIn = false;
    public static String authToken;
    private static ArrayList<GameData> gameList = new ArrayList<>();
    public static GameData currentGame;
    public ChessGame.TeamColor playerColor;
    public final String serverUrl;
    private String username;

    public static void main(String[] args) {
        new Client("http://localhost:8080");
    }

    public Client(String serverUrl) {
        this.serverUrl = serverUrl;
        serverFacade = new ServerFacade(8080);
        run();
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION:
//                Serializer
            case LOAD_GAME:
                currentGame = ((LoadGame) message).gameData();
                ChessBoardUI.drawChessBoards(currentGame.chessGame().getBoard());
//            case ERROR -> System.out.println(message.errorMessage());
        }
        System.out.println(message);
    }

    public void run() {
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

    public Object eval(String input) {
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

    public String leave() throws DataAccessException {
        if(currentGame == null) {
            return "You are not currently in a game";
        }

        currentGame = serverFacade.leave(this, currentGame, String.valueOf(playerColor));

//        if(Objects.equals(currentGame.whiteUsername(), username)) {
//            currentGame = new GameData(currentGame.gameID(), null, currentGame.blackUsername(), currentGame.gameName(), currentGame.chessGame(), null, currentGame.observerList());
//        }
//        else if(Objects.equals(currentGame.blackUsername(), username)) {
//            currentGame = new GameData(currentGame.gameID(), currentGame.whiteUsername(), null, currentGame.gameName(), currentGame.chessGame(), null, currentGame.observerList());
//        }
//        else {
//            ArrayList<UserData> newObserverList = new ArrayList<>(currentGame.observerList());
//            newObserverList.removeIf(user -> user.username().equals(username));
//            currentGame = new GameData(currentGame.gameID(), currentGame.whiteUsername(), currentGame.blackUsername(), currentGame.gameName(), currentGame.chessGame(), null, newObserverList);
//        }
        return "You have left the game";
    }

    public String resign() throws DataAccessException {
        currentGame = null;
        serverFacade.resign(this);
        return "You have resigned";
    }

    public String makeMove(String ...params) throws DataAccessException {
        if (params.length < 2) {
            return "Invalid Move Command";
        }
        ChessPosition fromPosition = parsePosition(params[0]);
        ChessPosition toPosition = parsePosition(params[1]);
        ChessMove move = new ChessMove(fromPosition, toPosition, null);
        serverFacade.makeMove(this, move);
        return "Move made";
    }
    public String redrawChessBoard() {
        ChessBoardUI.drawChessBoard(currentGame.chessGame().getBoard(), playerColor, null);
        return "";
    }

    public String highlightLegalMoves(String... params) {
        if (params.length < 1) {
            return "Please specify a position to highlight legal moves for.";
        }
        if (playerColor == null) {
            return "Please join a game as a player first";
        }
        ChessPosition position = parsePosition(params);
        ChessPiece piece = currentGame.chessGame().getBoard().getPiece(position);
        if (piece == null) {
            return "No piece at position " + params[0].toUpperCase();
        }
        else if (piece.getTeamColor() != playerColor) {
            return "You can only highlight legal moves for your own pieces.";
        }
        Collection<ChessMove> legalMoves = currentGame.chessGame().validMoves(position);
        ChessBoardUI.drawChessBoard(currentGame.chessGame().getBoard(), playerColor, legalMoves);
        return "Legal moves for " + params[0].toUpperCase() + " highlighted.";
    }

    public static ChessPosition parsePosition(String... params ) {
        char firstChar = params[0].charAt(0);
        int y = firstChar - 'a' + 1; // Convert alphabet to numeric value
        int x = Integer.parseInt(params[0].substring(1)); // Extract the numeric part of the string
        return new ChessPosition(x, y);
    }

    public String login(String... params) throws DataAccessException {
        if (params.length < 2) {
            throw new DataAccessException("Bad Request", 400);
        }
        UserData loginRequest = new UserData(params[0], params[1], null);
        AuthData loginResponse = serverFacade.login(loginRequest);
        loggedIn = true;
        authToken = loginResponse.authToken();
        this.username = loginResponse.username();
        System.out.println(loggedInHelp());
        return "You have been logged in.";
    }

    public String register(String... params) throws DataAccessException {
        if (params.length < 3) {
            throw new DataAccessException("Bad Request", 400);
        }
        UserData registerRequest = new UserData(params[0], params[1], params[2]);
        AuthData registerResponse = serverFacade.register(registerRequest);
        authToken = registerResponse.authToken();
        loggedIn = true;
        this.username = registerResponse.username();
        return "You have been registered and logged in.";
    }

    public static String logout() throws DataAccessException {
        serverFacade.logout(authToken);
        loggedIn = false;
        System.out.println(loggedOutHelp());
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
        currentGame = createGameResponse;
        return "Game " + params[0] + " created with ID: " + gameID;
    }

    public String joinRequest(String... params) throws DataAccessException {
        if (params.length < 1) {
            throw new DataAccessException("Bad Request", 400);
        }
        String color = null;
        if (params.length > 1) {
            color = params[1].toUpperCase();
        }
        GameData gameToJoin = gameList.get(Integer.parseInt(params[0]) - 1);
        int gameID = gameToJoin.gameID();
        GameData joinRequest = new GameData(gameID, null, null, null, null, color, null);
        GameData gameData = serverFacade.joinRequest(joinRequest, authToken);
        ChessBoard chessBoard = gameData.chessGame().getBoard();
        ChessBoardUI.drawChessBoards(chessBoard);
        currentGame = gameData;
        if (color == null) {
            serverFacade.webSocketJoinRequest(this, false);
            return "You have joined " + gameList.get(Integer.parseInt(params[0]) - 1).gameName() + " as an observer.";
        }
        else if (color.equals("BLACK")) {
            playerColor = ChessGame.TeamColor.BLACK;
            serverFacade.webSocketJoinRequest(this, true);
            return "You have joined the game as black.";
        }
        else if (color.equals("WHITE")) {
            playerColor = ChessGame.TeamColor.WHITE;
            serverFacade.webSocketJoinRequest(this, true);
            return "You have joined the game as white.";
        }
        return "Invalid color";
    }

    private String loggedInMenu(String cmd, String... params) throws DataAccessException {
        return switch (cmd) {
            case "1" -> logout();
            case "2" -> joinRequest(params);
            case "3" -> listGames();
            case "4" -> createGame(params);
            case "5" -> redrawChessBoard();
            case "6" -> leave();
            case "7" -> resign();
            case "8" -> highlightLegalMoves(params);
            case "9" -> makeMove();
            case "11" -> quit();
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
                  9. Make Move <position> <position>
                  10. Help
                  11. Quit
                """;
    }

    private String loggedOutMenu(String cmd, String... params) throws DataAccessException {
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

    private static String help() {
        if (loggedIn) {
            return loggedInHelp();
        } else {
            return loggedOutHelp();
        }
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

    private static String quit() {
        return "quit";
    }



}
