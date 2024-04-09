package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    // ConcurrentHashMap with gameID as key and list of connections as value
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connectionsByGameID = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connectionsByGameID.computeIfAbsent(gameID, k -> new ArrayList<>()).add(connection);
    }

    public void remove(int gameID, String authToken) {
        connectionsByGameID.getOrDefault(gameID, new ArrayList<>()).removeIf(connection -> connection.authToken.equals(authToken));
    }

    public void broadcast(int gameID, String excludeCurrentAuthToken, ServerMessage notification, Boolean excludeSender) throws IOException {
        var connections = connectionsByGameID.getOrDefault(gameID, new ArrayList<>());
        var removeList = new ArrayList<Connection>();
        for (var c : connections) {
            if (c.session.isOpen()) {
                //Only send error message to sender
                if (notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR && !c.authToken.equals(excludeCurrentAuthToken)) {
                    continue;
                }
                //Only send notifications to other users
                if (excludeSender && c.authToken.equals(excludeCurrentAuthToken)) {
                    continue;
                }
                c.send(new Gson().toJson(notification));
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c);
        }
    }
}
