package ServerClientCommunication;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

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
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    serverMessageHandler.notify(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void leave(String authToken) throws DataAccessException {
        try {
            var command = new UserGameCommand(authToken, UserGameCommand.CommandType.LEAVE);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    public void joinRequest(String authToken, Boolean joinAsPlayer) throws DataAccessException {
        try {
            if (joinAsPlayer) {
                var command = new UserGameCommand(authToken, UserGameCommand.CommandType.JOIN_PLAYER);
                this.session.getBasicRemote().sendText(new Gson().toJson(command));
            } else {
                var command = new UserGameCommand(authToken, UserGameCommand.CommandType.JOIN_OBSERVER);
                this.session.getBasicRemote().sendText(new Gson().toJson(command));
            }
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }
}
