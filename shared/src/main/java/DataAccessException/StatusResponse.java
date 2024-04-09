package DataAccessException;

import model.GameData;

import java.util.ArrayList;

public record StatusResponse(String message, int statusCode, String gameID, ArrayList<GameData> games) {
}

