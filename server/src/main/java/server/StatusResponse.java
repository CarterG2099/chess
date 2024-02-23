package server;

import model.GameData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public record StatusResponse (String message, int statusCode, String gameID, ArrayList<GameData> games){}

