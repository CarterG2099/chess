package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                connections.add(command.getAuthToken(), session);
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, command.getAuthToken() + " joined the game");
                connections.broadcast(command.getAuthToken(), notification);
            }
            case JOIN_OBSERVER -> {
                connections.add(command.getAuthToken(), session);
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, command.getAuthToken() + " joined the game as an observer");
                connections.broadcast(command.getAuthToken(), notification);
            }
            case LEAVE -> {
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, command.getAuthToken() + " left the game");
                connections.broadcast(command.getAuthToken(), notification);
            }
            case MAKE_MOVE -> {
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, command.getAuthToken() + " made a move");
                connections.broadcast(command.getAuthToken(), notification);
            }
            case RESIGN -> {
                connections.remove(command.getAuthToken());
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, command.getAuthToken() + " resigned");
                connections.broadcast(command.getAuthToken(), notification);
            }
        }
    }

//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}