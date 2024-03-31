package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    String message;
    public Notification(ServerMessageType type) {
        super(type);
    }
}
