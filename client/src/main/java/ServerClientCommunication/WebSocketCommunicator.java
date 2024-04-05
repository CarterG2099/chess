package ServerClientCommunication;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {
    Session session;
    ServerMessageObserver serverMessageHandler;
    public WebSocketCommunicator(String url, ServerMessageObserver serverMessageHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    // Need to parse it so that it is the correct type of ServerMessage
                    serverMessageHandler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void leave(String authToken, int gameID, String playerColor) throws DataAccessException {
        try {
            var leaveCommand = new Leave(authToken, gameID, playerColor);
//            var command = new UserGameCommand(authToken, Leave.CommandType.LEAVE);
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveCommand));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    public void joinRequest(String authToken, int gameID, ChessGame.TeamColor color, Boolean joinAsPlayer) throws DataAccessException {
        try {
            if (joinAsPlayer) {
                var joinCommand = new JoinPlayer(authToken,gameID, color);
//                var command = new UserGameCommand(authToken, UserGameCommand.CommandType.JOIN_PLAYER);
                this.session.getBasicRemote().sendText(new Gson().toJson(joinCommand));
            } else {
                var joinCommand = new JoinObserver(authToken,gameID);
//                var command = new UserGameCommand(authToken, UserGameCommand.CommandType.JOIN_OBSERVER);
                this.session.getBasicRemote().sendText(new Gson().toJson(joinCommand));
            }
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    public void resign(String authToken, int gameID) throws DataAccessException {
        try {
            var resignCommand = new Resign(authToken, gameID);
//            var command = new UserGameCommand(authToken, UserGameCommand.CommandType.RESIGN);
            this.session.getBasicRemote().sendText(new Gson().toJson(resignCommand));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws DataAccessException {
        try {
            var moveCommand = new MakeMove(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(moveCommand));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }
}
