package webSocketMessages.serverMessages;

public class Error extends ServerMessage {
    String errorMessage;
    public Error(ServerMessageType type) {
        super(type);
    }
}
