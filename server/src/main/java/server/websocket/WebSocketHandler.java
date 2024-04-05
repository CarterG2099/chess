package server.websocket;

import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.UserHandler;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

import java.io.IOException;

import static server.Serializer.interpretUserGameCommand;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = (UserGameCommand) interpretUserGameCommand(message);
        userService.validAuthToken(command.getAuthToken());
        UserData user = userService.getUser(command.getAuthToken());
        String username = user.username();
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                joinPlayer((JoinPlayer) command, username, session);
            }
            case JOIN_OBSERVER -> {
                joinObserver((JoinObserver) command, username, session);
            }
            case LEAVE -> {
                leave((Leave) command, username);
            }
            case MAKE_MOVE -> {
                makeMove((MakeMove) command, username);
            }
            case RESIGN -> {
                resign((Resign) command, username);
            }
            default -> connections.broadcast(command.getAuthToken(), new Error("Invalid command type"));
        }
    }

    private void joinPlayer(JoinPlayer command, String username, Session session) throws IOException, DataAccessException {
        connections.add(command.getAuthToken(), session);
        var game = new LoadGame(gameService.getGame(command.gameID()));
        connections.broadcast(command.getAuthToken(), game);
        var notification = new Notification(username + " joined the game as " + command.playerColor() + " player");
        connections.broadcast(command.getAuthToken(), notification);
    }

    private void joinObserver(JoinObserver command, String username, Session session) throws IOException, DataAccessException {
        connections.add(command.getAuthToken(), session);
        var game = new LoadGame(gameService.getGame(command.gameID()));
        connections.broadcast(command.getAuthToken(), game);
        var notification = new Notification(username + " joined the game as an observer");
        connections.broadcast(command.getAuthToken(), notification);
    }

    private void leave(Leave command, String username) throws IOException {
        connections.remove(command.getAuthToken());
        var notification = new Notification(username + " left the game");
        connections.broadcast(command.getAuthToken(), notification);
    }

    private void makeMove(MakeMove command, String username) throws IOException, DataAccessException {
        try {
            GameData newGameData = gameService.makeMove(command.gameID(), command.move());
            var game = new LoadGame(newGameData);
            connections.broadcast(command.getAuthToken(), game);

            ChessGame.TeamColor nextPlayerColor = newGameData.chessGame().getTeamTurn();
            ChessPiece.PieceType movedPiece = newGameData.chessGame().getBoard().getPiece(command.move().getEndPosition()).getPieceType();
            if (newGameData.chessGame().isInCheckmate(nextPlayerColor)) {
                var notification = new Notification("Checkmate! " + username + " won the game");
                connections.broadcast(command.getAuthToken(), notification);
            }
            else if (newGameData.chessGame().isInCheck(nextPlayerColor)) {
                var notification = new Notification(username + " moved their " + movedPiece + " and now your king is in check!");
                connections.broadcast(command.getAuthToken(), notification);
            }
            else if (newGameData.chessGame().isInStalemate(nextPlayerColor)) {
                var notification = new Notification("The game ended in a draw");
                connections.broadcast(command.getAuthToken(), notification);
            }
            else {
                var notification = new Notification(username + " moved their " + movedPiece);
                connections.broadcast(command.getAuthToken(), notification);
            }
        }
        catch (InvalidMoveException e) {
            var error = new Error("Invalid move: " + e.getMessage());
            connections.broadcast(command.getAuthToken(), error);
        }
    }

    private void resign(Resign command, String username) throws IOException {
        connections.remove(command.getAuthToken());
        var notification = new Notification(username + " resigned");
        connections.broadcast(command.getAuthToken(), notification);
    }
}