package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    public static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final ChessBoard board = new ChessBoard();

    public enum Orientation {
        WHITE,
        BLACK
    }

    public static void main(String[] args) {
        board.resetBoard();
        drawChessBoard(board, Orientation.WHITE);
        drawChessBoardSpacer(System.out);
        drawChessBoard(board, Orientation.BLACK);
    }

    public static void drawChessBoard(ChessBoard board, Orientation orientation) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out, orientation);
        drawBoardWithColumns(out, board, orientation);
        drawHeaders(out, orientation);
    }

    public static void drawChessBoards(ChessBoard board) {
        drawChessBoard(board, Orientation.WHITE);
        drawChessBoardSpacer(System.out);
        drawChessBoard(board, Orientation.BLACK);
    }

    private static void drawChessBoardSpacer(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(NEW_LINE);
    }

    private static void drawHeaders(PrintStream out, Orientation orientation) {
        out.print(SET_BG_COLOR_DARK_GREY);

        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        drawEmptyHeader(out);
        if (orientation == Orientation.WHITE) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        } else {
            for (int boardCol = BOARD_SIZE_IN_SQUARES - 1; boardCol >= 0; --boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        }

        drawEmptyHeader(out);
        out.print(RESET_BG_COLOR);
        out.print(NEW_LINE);
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(headerText);
    }

    private static void drawEmptyHeader(PrintStream out) {
        out.print("   ");
    }

    private static void drawBoardWithColumns(PrintStream out, ChessBoard board, Orientation orientation) {
        String[] columns = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        if (orientation == Orientation.WHITE) {
            for (int boardRow = BOARD_SIZE_IN_SQUARES; boardRow > 0; boardRow--) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                drawColumn(out, columns[boardRow - 1]);
                drawBoardRow(out, boardRow, board, orientation);
                drawColumn(out, columns[boardRow - 1]);
                out.print(SET_BG_COLOR_BLACK);
                out.print(NEW_LINE);
            }

        } else {
            for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES; boardRow++) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                drawColumn(out, columns[boardRow - 1]);
                drawBoardRow(out, boardRow, board, orientation);
                drawColumn(out, columns[boardRow - 1]);
                out.print(SET_BG_COLOR_BLACK);
                out.print(NEW_LINE);
            }
        }
    }

    private static void drawColumn(PrintStream out, String colText) {
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(colText);
    }

    private static void drawBoardRow(PrintStream out, int boardRow, ChessBoard board, Orientation orientation) {
        if (orientation == Orientation.WHITE) {
            for (int boardCol = 1; boardCol <= BOARD_SIZE_IN_SQUARES; boardCol++) {
                int tileToInt = getTileColor(boardRow, boardCol);
                ChessPosition position = new ChessPosition(boardRow, boardCol);
                ChessPiece piece = board.getPiece(position);
                drawTile(out, piece, tileToInt);
            }
        } else {
            for (int boardCol = BOARD_SIZE_IN_SQUARES; boardCol > 0; boardCol--) {
                int tileToInt = getTileColor(boardRow, boardCol);
                ChessPosition position = new ChessPosition(boardRow, boardCol);
                ChessPiece piece = board.getPiece(position);
                drawTile(out, piece, tileToInt);
            }
        }
    }

    private static int getTileColor(int boardRow, int boardCol) {
        return (boardRow + boardCol) % 2;
    }

    private static void drawTile(PrintStream out, ChessPiece piece, int tileToInt) {
        if (tileToInt == 0) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
        } else {
            out.print(SET_BG_COLOR_BLUE);
        }
        if (piece == null) {
            out.print("   ");
        } else {
            drawChessPiece(out, piece);
        }
    }

    private static void drawChessPiece(PrintStream out, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_WHITE);
        } else {
            out.print(SET_TEXT_COLOR_BLACK);
        }
        switch (piece.getPieceType()) {
            case PAWN -> out.print(" P ");
            case KNIGHT -> out.print(" N ");
            case BISHOP -> out.print(" B ");
            case ROOK -> out.print(" R ");
            case QUEEN -> out.print(" Q ");
            case KING -> out.print(" K ");
        }
    }
}