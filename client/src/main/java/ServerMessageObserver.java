import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageObserver {
    void notify(ServerMessage message);
}
