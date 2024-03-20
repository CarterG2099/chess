package model;

import chess.ChessGame;

import java.util.ArrayList;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame, String playerColor, ArrayList<UserData> observerList) { }
