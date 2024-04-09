package server.websocket;

import chess.ChessGame;
import chess.ChessPiece;
import DataAccessException.DataAccessException;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

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
            default -> connections.broadcast(0, command.getAuthToken(), new Error("Invalid command type"), false);
        }
    }

    private void joinPlayer(JoinPlayer command, String username, Session session) throws IOException, DataAccessException {
        try {
            userService.validAuthToken(command.getAuthToken());
            UserData user = userService.getUser(command.getAuthToken());
            gameService.joinGame(command.gameData(), user);

            var game = new LoadGame(gameService.getGame(command.gameID()));
            connections.add(command.gameID(), command.getAuthToken(), session);
            var notification = new Notification(username + " joined the game as " + command.playerColor() + " player");
            connections.broadcast(command.gameID(), command.getAuthToken(), game, false);
            connections.broadcast(command.gameID(), command.getAuthToken(), notification, true);
        } catch (Exception e) {
            connections.broadcast(command.gameID(), command.getAuthToken(), new Error(e.getMessage()), false);
        }
    }

    private void joinObserver(JoinObserver command, String username, Session session) throws IOException, DataAccessException {
        try {
            userService.validAuthToken(command.getAuthToken());
            UserData user = userService.getUser(command.getAuthToken());
            gameService.joinGame(command.gameData(), user);

            var game = new LoadGame(gameService.getGame(command.gameID()));
            connections.add(command.gameID(), command.getAuthToken(), session);
            var notification = new Notification(username + " joined the game as an observer");
            connections.broadcast(command.gameID(), command.getAuthToken(), game, false);
            connections.broadcast(command.gameID(), command.getAuthToken(), notification, true);
        } catch (Exception e) {
            connections.broadcast(command.gameID(), command.getAuthToken(), new Error(e.getMessage()), false);
        }
    }

    private void leave(Leave command, String username) throws IOException, DataAccessException {
        try {
            userService.validAuthToken(command.getAuthToken());
            gameService.leaveGame(command.playerColor(), command.gameID());
            connections.remove(command.gameID(), command.getAuthToken());

            var game = new LoadGame(gameService.getGame(command.gameID()));
            connections.broadcast(command.gameID(), command.getAuthToken(), game, false);
            var notification = new Notification(username + " left the game");
            connections.broadcast(command.gameID(), command.getAuthToken(), notification, true);
        } catch (Exception e) {
            connections.broadcast(command.gameID(), command.getAuthToken(), new Error(e.getMessage()), false);
        }
    }

    private void makeMove(MakeMove command, String username) throws IOException, DataAccessException {
        try {
            userService.validAuthToken(command.getAuthToken());
            GameData gameToVerify = gameService.getGame(command.gameID());
            UserData userToVerify = userService.getUser(command.getAuthToken());
            if (!Objects.equals(gameToVerify.blackUsername(), userToVerify.username()) && !Objects.equals(gameToVerify.whiteUsername(), userToVerify.username())) {
                connections.broadcast(command.gameID(), command.getAuthToken(), new Error("You are not a player in this game"), false);
                return;
            }

            GameData newGameData = gameService.makeMove(command.gameID(), command.move());
            var game = new LoadGame(newGameData);
            connections.broadcast(command.gameID(), command.getAuthToken(), game, false);

            ChessGame.TeamColor nextPlayerColor = newGameData.chessGame().getTeamTurn();
            ChessPiece.PieceType movedPiece = newGameData.chessGame().getBoard().getPiece(command.move().getEndPosition()).getPieceType();
            if (newGameData.chessGame().isInCheckmate(nextPlayerColor)) {
                var notification = new Notification("Checkmate! " + username + " won the game");
                connections.broadcast(command.gameID(), command.getAuthToken(), notification, true);
            } else if (newGameData.chessGame().isInCheck(nextPlayerColor)) {
                var notification = new Notification(username + " moved their " + movedPiece + " and now your king is in check!");
                connections.broadcast(command.gameID(), command.getAuthToken(), notification, true);
            } else if (newGameData.chessGame().isInStalemate(nextPlayerColor)) {
                var notification = new Notification("The game ended in a draw");
                connections.broadcast(command.gameID(), command.getAuthToken(), notification, true);
            } else {
                var notification = new Notification(username + " moved their " + movedPiece);
                connections.broadcast(command.gameID(), command.getAuthToken(), notification, true);
            }
        } catch (Exception e) {
            var error = new Error("Invalid move: " + e.getMessage());
            connections.broadcast(command.gameID(), command.getAuthToken(), error, false);
        }
    }

    private void resign(Resign command, String username) throws IOException {
        connections.remove(command.gameID(), command.getAuthToken());
        var notification = new Notification(username + " resigned");
        connections.broadcast(command.gameID(), command.getAuthToken(), notification, true);
    }
}