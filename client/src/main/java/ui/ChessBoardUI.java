package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    public static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final ChessBoard board = new ChessBoard();

//    public enum Orientation {
//        WHITE,
//        BLACK
//    }

    public static void main(String[] args) {
        board.resetBoard();
        drawChessBoard(board, ChessGame.TeamColor.WHITE, null);
        drawChessBoardSpacer(System.out);
        drawChessBoard(board, ChessGame.TeamColor.BLACK, null);
    }

    public static void drawChessBoard(ChessBoard board, ChessGame.TeamColor orientation, Collection<ChessMove> highlightedPositions) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out, orientation);
        drawBoardWithColumns(out, board, orientation, highlightedPositions);
        drawHeaders(out, orientation);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    public static void drawChessBoards(ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawChessBoard(board, ChessGame.TeamColor.WHITE, null);
        drawChessBoardSpacer(out);
        drawChessBoard(board, ChessGame.TeamColor.BLACK, null);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawChessBoardSpacer(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(NEW_LINE);
    }

    private static void drawHeaders(PrintStream out, ChessGame.TeamColor orientation) {
        out.print(SET_BG_COLOR_DARK_GREY);

        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        drawEmptyHeader(out);
        if (orientation == ChessGame.TeamColor.WHITE) {
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

    private static void drawBoardWithColumns(PrintStream out, ChessBoard board, ChessGame.TeamColor orientation, Collection<ChessMove> highlightedPositions) {
        String[] columns = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        if (orientation == ChessGame.TeamColor.WHITE) {
            for (int boardRow = BOARD_SIZE_IN_SQUARES; boardRow > 0; boardRow--) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                drawColumn(out, columns[boardRow - 1]);
                drawBoardRow(out, boardRow, board, orientation, highlightedPositions);
                drawColumn(out, columns[boardRow - 1]);
                out.print(SET_BG_COLOR_BLACK);
                out.print(NEW_LINE);
            }

        } else {
            for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES; boardRow++) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                drawColumn(out, columns[boardRow - 1]);
                drawBoardRow(out, boardRow, board, orientation, highlightedPositions);
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

    private static void drawBoardRow(PrintStream out, int boardRow, ChessBoard board, ChessGame.TeamColor orientation, Collection<ChessMove> highlightedPositions) {
        if (orientation == ChessGame.TeamColor.WHITE) {
            for (int boardCol = 1; boardCol <= BOARD_SIZE_IN_SQUARES; boardCol++) {
                int tileToInt = getTileColor(boardRow, boardCol, highlightedPositions);
                ChessPosition position = new ChessPosition(boardRow, boardCol);
                ChessPiece piece = board.getPiece(position);
                drawTile(out, piece, tileToInt);
            }
        } else {
            for (int boardCol = BOARD_SIZE_IN_SQUARES; boardCol > 0; boardCol--) {
                int tileToInt = getTileColor(boardRow, boardCol, highlightedPositions);
                ChessPosition position = new ChessPosition(boardRow, boardCol);
                ChessPiece piece = board.getPiece(position);
                drawTile(out, piece, tileToInt);
            }
        }
    }

    private static int getTileColor(int boardRow, int boardCol, Collection<ChessMove> highlightedPositions) {
        if(highlightedPositions != null) {
            for (ChessMove move : highlightedPositions) {
                if(move.getStartPosition().getRow() == boardRow && move.getStartPosition().getColumn() == boardCol) {
                    return 3;
                }
                if (move.getEndPosition().getRow() == boardRow && move.getEndPosition().getColumn() == boardCol) {
                    return 2;
                }
            }
        }
        return (boardRow + boardCol) % 2;
    }

    private static void drawTile(PrintStream out, ChessPiece piece, int tileToInt) {
        String textColor = null;
        if (tileToInt == 3) {
            out.print(SET_BG_COLOR_YELLOW);
            textColor = SET_TEXT_COLOR_BLUE;
        }
        else if (tileToInt == 2) {
            out.print(SET_BG_COLOR_GREEN);
            textColor = SET_TEXT_COLOR_RED;
        }
        else if (tileToInt == 0) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
        } else {
            out.print(SET_BG_COLOR_BLUE);
        }

        if (piece == null) {
            out.print("   ");
        } else {
            drawChessPiece(out, piece, textColor);
        }
    }

    private static void drawChessPiece(PrintStream out, ChessPiece piece, String textColor) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(Objects.requireNonNullElse(textColor, SET_TEXT_COLOR_WHITE));
        } else {
            out.print(Objects.requireNonNullElse(textColor, SET_TEXT_COLOR_BLACK));
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