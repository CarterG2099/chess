package chess.MovesCalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    private final Collection<ChessMove> chessMoveCollection = new ArrayList<ChessMove>();

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        int[][] directions = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
        for (int[] move : directions) {
            int row = position.getRow() + move[0];
            int col = position.getColumn() + move[1];
            if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);
                if (pieceAtPosition == null) chessMoveCollection.add(new ChessMove(position, newPosition, null));
                else if (pieceAtPosition.getTeamColor() != color)
                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
            }
        }
        return chessMoveCollection;
    }
}
