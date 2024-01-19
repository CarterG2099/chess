package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculaor implements PieceMovesCalculator{

    private final Collection<ChessMove> chessMoveCollection = new ArrayList<ChessMove>();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        return null;
    }
}
