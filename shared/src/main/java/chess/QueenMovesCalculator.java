package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {

    private final Collection<ChessMove> chessMoveCollection = new ArrayList<>();

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        chessMoveCollection.addAll(new BishopMovesCalculator().pieceMoves(board, position, color));
        chessMoveCollection.addAll(new RookMovesCalculator().pieceMoves(board, position, color));
        return chessMoveCollection;
    }
}
