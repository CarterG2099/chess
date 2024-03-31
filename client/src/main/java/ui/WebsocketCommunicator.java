package ui;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

@WebSocket
public class WebsocketCommunicator {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WebsocketCommunicator.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        GameCommand command = readJson(message, UserGameCommand.class);

        var conn = getConnection(command.authToken(), session);
        if (conn != null) {
            switch (command.commandType) {
            }
        }
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }
}
