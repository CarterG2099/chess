package server;

import DataAccessException.DataAccessException;
import com.google.gson.Gson;
import spark.Response;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import DataAccessException.StatusResponse;

public class Serializer {
    public static final Gson gson = new Gson();

    public static Object translateExceptionToJson(DataAccessException ex, Response res) {
        if (res != null) {
            res.status(ex.getStatusCode());
            res.body(ex.getMessage());
        }
        StatusResponse statusResponse = new StatusResponse(ex.getMessage(), ex.getStatusCode(), null, null);
        return gson.toJson(statusResponse);
    }

    public static Object translateSuccessToJson(Response res) {
        StatusResponse statusResponse = new StatusResponse("Success", 200, null, null);
        res.status(200);
        res.body("Success");
        return gson.toJson(statusResponse);
    }

    public static Object interpretServerMessage(String message) {
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        return switch (serverMessage.getServerMessageType()) {
            case ERROR -> gson.fromJson(message, Error.class);
            case NOTIFICATION -> gson.fromJson(message, Notification.class);
            case LOAD_GAME -> gson.fromJson(message, LoadGame.class);
            default -> null;
        };
    }

    public static Object interpretUserGameCommand(String message) {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        return switch (command.getCommandType()) {
            case JOIN_PLAYER -> interpretJoinPlayer(message);
            case JOIN_OBSERVER -> interpretJoinObserver(message);
            case LEAVE -> interpretLeave(message);
            case MAKE_MOVE -> interpretMakeMove(message);
            case RESIGN -> interpretResign(message);
        };
    }

    private static Leave interpretLeave(String message) {
        return gson.fromJson(message, Leave.class);
    }

    private static JoinObserver interpretJoinObserver(String message) {
        return gson.fromJson(message, JoinObserver.class);
    }

    private static JoinPlayer interpretJoinPlayer(String message) {
        return gson.fromJson(message, JoinPlayer.class);
    }

    private static MakeMove interpretMakeMove(String message) {
        return gson.fromJson(message, MakeMove.class);
    }

    private static Resign interpretResign(String message) {
        return gson.fromJson(message, Resign.class);
    }
}
