package ServerClientCommunication;

import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageObserver {
    void notify(String message);
}
