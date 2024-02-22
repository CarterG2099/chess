package model;

import chess.ChessGame;

public record GameData(String gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) { }
