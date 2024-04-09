//package ServerClientCommunication;
//
//import com.google.gson.Gson;
//import DataAccessException.DataAccessException;
//import webSocketMessages.serverMessages.ServerMessage;
//import webSocketMessages.userCommands.UserGameCommand;
//
//import javax.websocket.*;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//
////need to extend Endpoint for websocket to work properly
//public class WebSocketCommunicator extends Endpoint {
//
//    Session session;
//    ServerMessageObserver serverMessageHandler;
//
//
//    public WebSocketCommunicator(String url, ServerMessageObserver serverMessageHandler) throws DataAccessException.DataAccessException {
//        try {
//            url = url.replace("http", "ws");
//            URI socketURI = new URI(url + "/connect");
//            this.serverMessageHandler = serverMessageHandler;
//
//            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//            this.session = container.connectToServer(this, socketURI);
//
//            //set message handler
//            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
//                @Override
//                public void onMessage(String message) {
//                    // Need to parse it so that it is the correct type of ServerMessage
//                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
//                    serverMessageHandler.notify(serverMessage);
//                }
//            });
//        } catch (DeploymentException | IOException | URISyntaxException ex) {
//            throw new DataAccessException.DataAccessException(ex.getMessage(), 500);
//        }
//    }
//
//    //Endpoint requires this method, but you don't have to do anything
//    @Override
//    public void onOpen(Session session, EndpointConfig endpointConfig) {
//    }
//
//    public void leave() throws DataAccessException.DataAccessException {
//        try {
//            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, UserGameCommand.CommandType.LEAVE);
//            this.session.getBasicRemote().sendText(new Gson().toJson(command));
//        } catch (IOException ex) {
//            throw new DataAccessException.DataAccessException(ex.getMessage(), 500);
//        }
//    }
//}
//
//public void joinRequest(ServerMessageObserver serverMessage) {
//    try {
//        var command = new UserGameCommand(serverMessage.authToken, UserGameCommand.CommandType.LEAVE);
//        this.session.getBasicRemote().sendText(new Gson().toJson(command));
//    } catch (IOException ex) {
//        throw new DataAccessException.DataAccessException(ex.getMessage(), 500);
//    }
//}
//
////    public void enterPetShop(String visitorName) throws ResponseException {
////        try {
////            var action = new Action(Action.Type.ENTER, visitorName);
////            this.session.getBasicRemote().sendText(new Gson().toJson(action));
////        } catch (IOException ex) {
////            throw new ResponseException(500, ex.getMessage());
////        }
////    }
////
////    public void leavePetShop(String visitorName) throws ResponseException {
////        try {
////            var action = new Action(Action.Type.EXIT, visitorName);
////            this.session.getBasicRemote().sendText(new Gson().toJson(action));
////            this.session.close();
////        } catch (IOException ex) {
////            throw new ResponseException(500, ex.getMessage());
////        }
////    }
//
//}
//
//public void main() {
//}