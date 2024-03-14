package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class ChessBoardUI {

    public static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 2;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";
    private static final Random rand = new Random();
    private static final ChessBoard board = new ChessBoard();
    public static void main(String[] args) {
        drawChessBoard(board);
    }

    public static void drawChessBoard(ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out);
        drawBoardWithColumns(out, board);
        drawHeaders(out);
    }

    private static void drawHeaders(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);

        String[] headers = {" a "," b "," c "," d "," e "," f "," g "," h "};
        drawEmptyHeader(out);
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++){
            drawHeader(out, headers[boardCol]);
        }
        drawEmptyHeader(out);
        out.print(SET_BG_COLOR_BLACK);
        out.print(NEW_LINE);
    }

    private static void drawHeader(PrintStream out, String headerText){
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(headerText);
    }

    private static void drawEmptyHeader(PrintStream out){
        out.print(EMPTY);
    }

    private static void drawBoardWithColumns(PrintStream out, ChessBoard board){
        String[] columns = {" 1 "," 2 "," 3 "," 4 "," 5 "," 6 "," 7 "," 8 "};
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++){
            out.print(SET_BG_COLOR_LIGHT_GREY);
            drawColumn(out, columns[boardCol]);
            drawBoardRow(out, boardCol, board);
            drawColumn(out, columns[boardCol]);
            out.print(SET_BG_COLOR_BLACK);
            out.print(NEW_LINE);
        }
    }

    private static void drawColumn(PrintStream out, String colText) {
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(colText);
    }

    private static void drawBoardRow(PrintStream out, int rowNum, ChessBoard board){
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++){
            ChessPosition position = new ChessPosition(boardRow + 1, rowNum + 1);
            drawTile(out, board.getPiece(position));
        }
    }

    private static void drawTile(PrintStream out, ChessPiece piece){
        if (piece == null){
            out.print(EMPTY);
        }
        else {
            drawChessPiece(out, piece);
        }
    }

    private static void drawChessPiece(PrintStream out, ChessPiece piece){
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            switch (piece.getPieceType()) {
                case PAWN -> out.print(WHITE_PAWN);
                case KNIGHT -> out.print(WHITE_KNIGHT);
                case BISHOP -> out.print(WHITE_BISHOP);
                case ROOK -> out.print(WHITE_ROOK);
                case QUEEN -> out.print(WHITE_QUEEN);
                case KING -> out.print(WHITE_KING);
                default -> out.print(EMPTY);
            }
        }
        else {
            switch (piece.getPieceType()) {
                case PAWN -> out.print(BLACK_PAWN);
                case KNIGHT -> out.print(BLACK_KNIGHT);
                case BISHOP -> out.print(BLACK_BISHOP);
                case ROOK -> out.print(BLACK_ROOK);
                case QUEEN -> out.print(BLACK_QUEEN);
                case KING -> out.print(BLACK_KING);
                default -> out.print(EMPTY);
            }
        }    }

    private static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }


}

//How to get user input
// Scanner scanner = new Scanner(System.in);
// String line = scanner.nextLine();